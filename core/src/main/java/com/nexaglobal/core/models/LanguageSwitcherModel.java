package com.nexaglobal.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

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

        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        if (pageManager == null) {
            return;
        }

        Page currentPage = pageManager.getContainingPage(resource);
        if (currentPage == null) {
            return;
        }

        // Determine site root - use configured or detect from current page
        String root = siteRoot;
        if (root == null || root.isEmpty()) {
            // Try to detect: /content/nexaglobal is the site root
            String pagePath = currentPage.getPath();
            String[] segments = pagePath.split("/");
            if (segments.length >= 3) {
                root = "/" + segments[1] + "/" + segments[2]; // /content/nexaglobal
            }
        }

        if (root == null) {
            return;
        }

        Page rootPage = pageManager.getPage(root);
        if (rootPage == null) {
            return;
        }

        // Get current page's relative path from its language root
        String currentPath = currentPage.getPath();
        String currentLangRoot = findLanguageRoot(currentPath, root);

        // Iterate over region/language children
        for (Page regionPage : (Iterable<Page>) () -> rootPage.listChildren()) {
            for (Page langPage : (Iterable<Page>) () -> regionPage.listChildren()) {
                Locale locale = langPage.getLanguage(false);
                String langTitle = locale != null ? locale.getDisplayLanguage(Locale.ENGLISH) : langPage.getTitle();
                String langCode = langPage.getName().toUpperCase();
                boolean isActive = currentPath.startsWith(langPage.getPath());

                // Build equivalent page path in this language
                String targetPath = langPage.getPath();
                if (currentLangRoot != null) {
                    String relativePath = currentPath.substring(currentLangRoot.length());
                    String candidatePath = langPage.getPath() + relativePath;
                    if (pageManager.getPage(candidatePath) != null) {
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

    private String findLanguageRoot(String pagePath, String siteRoot) {
        // Language root is typically 2 levels below site root: /content/nexaglobal/us/en
        PageManager pm = resourceResolver.adaptTo(PageManager.class);
        String[] segments = pagePath.substring(siteRoot.length()).split("/");
        if (segments.length >= 3 && pm != null) {
            return siteRoot + "/" + segments[1] + "/" + segments[2];
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
