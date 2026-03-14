# NexaGlobal Implementation Plan

## Goal

Build the NexaGlobal multi-region corporate platform on top of the generated AEM as a
Cloud Service archetype project.

This implementation is based on the business and technical scope described in:

- `/Users/hemanthchowdary/Downloads/nexaglobal-project-doc.md`

## Current Baseline

The generated project already provides:

- AEMaaCS-compatible Maven multi-module structure
- `core`, `ui.apps`, `ui.content`, `ui.config`, `dispatcher`, `it.tests`, `ui.tests`
- proxy components for several Core Components
- editable template baseline
- Cloud-ready module layout

What still needs to be built is the NexaGlobal-specific application structure.

## Target Scope

### Sites and Regions

- US master / blueprint
- UK live copy
- FR, DE, JP regional sites

### Key page types

- Homepage
- Content page
- Article page

### Key custom features

- Header
- Footer
- Hero Banner
- Card List
- Accordion
- Tabs
- Search
- Article List
- Breadcrumb
- Language Switcher

### Headless content

- Article Content Fragment Model
- Team Member Content Fragment Model
- JSON delivery for article content

### Enterprise features

- region-specific analytics via Context-Aware Configuration
- approval workflow for article publishing
- environment variables for service configuration
- Dispatcher caching and filtering

## Delivery Phases

### Phase 1: Local foundation

1. Build the archetype project successfully.
2. Start local AEM author and publish.
3. Validate package deployment.
4. Run Dispatcher locally.

### Phase 2: Content architecture

1. Define `/content/nexaglobal` site roots.
2. Define `/content/dam/nexaglobal`.
3. Create `/conf/nexaglobal` template and policy structure.
4. Define Content Fragment Models under `/conf/nexaglobal/settings/dam/cfm/models`.

### Phase 3: Page and component structure

1. Create `basepage` component as project page base.
2. Create `homepage`, `contentpage`, `articlepage`.
3. Add structure components:
   - header
   - footer
   - breadcrumb
   - language-switcher
4. Add content components:
   - hero
   - card-list
   - teaser
   - accordion
   - tabs
   - search
   - article-list

### Phase 4: Backend implementation

1. Add Sling Models:
   - `HeaderModel`
   - `FooterModel`
   - `HeroModel`
   - `CardListModel`
   - `SearchModel`
   - `ArticleModel`
   - `BreadcrumbModel`
   - `LanguageSwitcherModel`
2. Add services:
   - `SearchService`
   - `AnalyticsService`
   - `EmailService`
3. Add servlets:
   - `SearchServlet`
   - `ContentFragmentServlet`
4. Add support classes:
   - DTOs for cards, footer links, search results
   - config interfaces for CA Config and OSGi

### Phase 5: Multi-site and localization

1. Create US master structure.
2. Configure UK live copy.
3. Create FR, DE, JP localized trees.
4. Implement language navigation logic.
5. Add i18n dictionaries.

### Phase 6: Headless and search

1. Create CF Models.
2. Build article listing and article detail consumption logic.
3. Implement search servlet with debounced frontend integration.
4. Expose selected JSON endpoints for mobile/external use.

### Phase 7: Workflow, analytics, and governance

1. Add article approval workflow.
2. Add region-specific analytics config via Context-Aware Configuration.
3. Add service-user mappings and permissions.
4. Add audit-friendly logging where needed.

### Phase 8: Dispatcher, testing, and polish

1. Tighten Dispatcher filters and cache rules.
2. Add AEM Mocks unit tests for Sling Models and services.
3. Add integration tests for key endpoints.
4. Add basic Cypress author/publish validation.

## Recommended Build Order

Do the implementation in this order:

1. `basepage`
2. templates and policies
3. header/footer
4. hero and card-list
5. breadcrumb and language switcher
6. article page and CF models
7. search service and servlet
8. workflow and analytics configuration
9. dispatcher hardening
10. tests

## Immediate Next Steps

1. Run a full Maven build locally from the project root.
2. Start local AEM author and publish instances.
3. Deploy the generated package to author.
4. Replace the sample page/template content with NexaGlobal content structure.
5. Start implementing the `basepage`, `homepage`, and `header` components first.

## Suggested Working Conventions

### Java packages

- `com.nexaglobal.core.models`
- `com.nexaglobal.core.services`
- `com.nexaglobal.core.services.impl`
- `com.nexaglobal.core.servlets`
- `com.nexaglobal.core.filters`
- `com.nexaglobal.core.schedulers`
- `com.nexaglobal.core.beans`

### Component paths

- `/apps/nexaglobal/components/page/basepage`
- `/apps/nexaglobal/components/page/homepage`
- `/apps/nexaglobal/components/page/contentpage`
- `/apps/nexaglobal/components/page/articlepage`
- `/apps/nexaglobal/components/structure/header`
- `/apps/nexaglobal/components/structure/footer`
- `/apps/nexaglobal/components/structure/breadcrumb`
- `/apps/nexaglobal/components/structure/language-switcher`
- `/apps/nexaglobal/components/content/hero`
- `/apps/nexaglobal/components/content/card-list`
- `/apps/nexaglobal/components/content/teaser`
- `/apps/nexaglobal/components/content/accordion`
- `/apps/nexaglobal/components/content/tabs`
- `/apps/nexaglobal/components/content/search`
- `/apps/nexaglobal/components/content/article-list`

## Definition of Done for Milestone 1

Milestone 1 is complete when:

- the project builds locally
- the package deploys to local author
- homepage template exists
- base page works
- header and footer render
- one homepage under `/content/nexaglobal/us/en` is authorable
