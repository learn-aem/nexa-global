package com.nexaglobal.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LanguageSwitcherModel {

    @SlingObject
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue
    private String siteRoot;

    private List<Language> languages;
    private String currentLanguage;

    @PostConstruct
    protected void init() {
        languages = new ArrayList<>();
        currentLanguage = "EN";

        // Find the current page path by walking up from component resource
        String resourcePath = resource.getPath();
        String currentPagePath = extractPagePath(resourcePath);
        if (currentPagePath == null) {
            return;
        }

        // Determine site root
        String root = siteRoot;
        if (root == null || root.isEmpty()) {
            String[] segments = currentPagePath.split("/");
            if (segments.length >= 3) {
                root = "/" + segments[1] + "/" + segments[2];
            }
        }

        if (root == null) {
            return;
        }

        Resource rootResource = resourceResolver.getResource(root);
        if (rootResource == null) {
            return;
        }

        // Find current language root: /content/nexaglobal/us/en
        String currentLangRoot = findLanguageRoot(currentPagePath, root);

        // Iterate region > language children
        for (Resource regionResource : rootResource.getChildren()) {
            Resource regionJcr = regionResource.getChild("jcr:content");
            if (regionJcr == null) {
                continue;
            }

            for (Resource langResource : regionResource.getChildren()) {
                Resource langJcr = langResource.getChild("jcr:content");
                if (langJcr == null) {
                    continue;
                }

                ValueMap langProps = langJcr.getValueMap();
                String langTitle = langProps.get("jcr:title", langResource.getName());
                String langCode = langResource.getName().toUpperCase();
                boolean isActive = currentPagePath.startsWith(langResource.getPath());

                // Build equivalent page path in this language
                String targetPath = langResource.getPath();
                if (currentLangRoot != null && currentPagePath.length() > currentLangRoot.length()) {
                    String relativePath = currentPagePath.substring(currentLangRoot.length());
                    String candidatePath = langResource.getPath() + relativePath;
                    if (resourceResolver.getResource(candidatePath) != null) {
                        targetPath = candidatePath;
                    }
                }

                languages.add(new Language(langTitle, langCode, targetPath, isActive));
                if (isActive) {
                    currentLanguage = langCode;
                }
            }
        }
    }

    private String extractPagePath(String resourcePath) {
        // Walk up from resource path to find the cq:Page (has jcr:content child)
        int jcrContentIdx = resourcePath.indexOf("/jcr:content");
        if (jcrContentIdx > 0) {
            return resourcePath.substring(0, jcrContentIdx);
        }
        return null;
    }

    private String findLanguageRoot(String pagePath, String root) {
        String relative = pagePath.substring(root.length());
        String[] segments = relative.split("/");
        // segments[0] = "", segments[1] = region, segments[2] = language
        if (segments.length >= 3) {
            return root + "/" + segments[1] + "/" + segments[2];
        }
        return null;
    }

    public List<Language> getLanguages() {
        return Collections.unmodifiableList(languages);
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public static class Language {
        private final String title;
        private final String code;
        private final String path;
        private final boolean active;

        public Language(String title, String code, String path, boolean active) {
            this.title = title;
            this.code = code;
            this.path = path;
            this.active = active;
        }

        public String getTitle() { return title; }
        public String getCode() { return code; }
        public String getPath() { return path; }
        public boolean isActive() { return active; }
    }
}
