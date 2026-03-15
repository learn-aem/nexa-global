package com.nexaglobal.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CardListModel {

    @SlingObject
    private Resource resource;

    private List<Card> cards;

    @PostConstruct
    protected void init() {
        cards = new ArrayList<>();
        Resource cardsResource = resource.getChild("cards");
        if (cardsResource != null) {
            for (Resource cardResource : cardsResource.getChildren()) {
                String title = cardResource.getValueMap().get("title", String.class);
                if (title != null) {
                    cards.add(new Card(
                        title,
                        cardResource.getValueMap().get("description", String.class),
                        cardResource.getValueMap().get("imagePath", String.class),
                        cardResource.getValueMap().get("linkUrl", String.class),
                        cardResource.getValueMap().get("linkLabel", String.class)
                    ));
                }
            }
        }
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public static class Card {
        private final String title;
        private final String description;
        private final String imagePath;
        private final String linkUrl;
        private final String linkLabel;

        public Card(String title, String description, String imagePath, String linkUrl, String linkLabel) {
            this.title = title;
            this.description = description;
            this.imagePath = imagePath;
            this.linkUrl = linkUrl;
            this.linkLabel = linkLabel;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getImagePath() { return imagePath; }
        public String getLinkUrl() { return linkUrl; }
        public String getLinkLabel() { return linkLabel; }
    }
}
