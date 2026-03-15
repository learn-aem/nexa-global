package com.nexaglobal.core.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleListModel {

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue
    private String parentPath;

    @ValueMapValue
    @Default(intValues = 10)
    private int maxItems;

    private List<Article> articles;

    @PostConstruct
    protected void init() {
        articles = new ArrayList<>();
        if (parentPath == null || parentPath.isEmpty()) {
            return;
        }

        Resource parentResource = resourceResolver.getResource(parentPath);
        if (parentResource == null) {
            return;
        }

        int count = 0;
        for (Resource child : parentResource.getChildren()) {
            if (count >= maxItems) {
                break;
            }
            // Only include cq:Page nodes
            Resource jcrContent = child.getChild("jcr:content");
            if (jcrContent == null) {
                continue;
            }

            ValueMap props = jcrContent.getValueMap();
            String title = props.get("jcr:title", child.getName());
            String description = props.get("jcr:description", String.class);
            String path = child.getPath();
            Calendar lastMod = props.get("cq:lastModified", Calendar.class);
            if (lastMod == null) {
                lastMod = props.get("jcr:lastModified", Calendar.class);
            }

            articles.add(new Article(title, description, path, formatDate(lastMod)));
            count++;
        }
    }

    private String formatDate(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        return sdf.format(calendar.getTime());
    }

    public List<Article> getArticles() {
        return Collections.unmodifiableList(articles);
    }

    public static class Article {
        private final String title;
        private final String description;
        private final String path;
        private final String lastModified;

        public Article(String title, String description, String path, String lastModified) {
            this.title = title;
            this.description = description;
            this.path = path;
            this.lastModified = lastModified;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getPath() { return path; }
        public String getLastModified() { return lastModified; }
    }
}
