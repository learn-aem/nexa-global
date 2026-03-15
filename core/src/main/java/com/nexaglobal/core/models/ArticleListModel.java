package com.nexaglobal.core.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

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

        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        if (pageManager == null) {
            return;
        }

        Page parentPage = pageManager.getPage(parentPath);
        if (parentPage == null) {
            return;
        }

        int count = 0;
        for (Page child : (Iterable<Page>) () -> parentPage.listChildren()) {
            if (count >= maxItems) {
                break;
            }
            String title = child.getTitle() != null ? child.getTitle() : child.getName();
            String description = child.getDescription();
            String path = child.getPath();
            String lastModified = formatDate(child.getLastModified());
            articles.add(new Article(title, description, path, lastModified));
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
