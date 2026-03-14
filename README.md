# NexaGlobal AEM Application

This repository is the local AEM as a Cloud Service project for the NexaGlobal
multi-region corporate platform.

The application goal is to implement the architecture defined in:

- [`docs/NEXAGLOBAL-IMPLEMENTATION-PLAN.md`](docs/NEXAGLOBAL-IMPLEMENTATION-PLAN.md)
- `/Users/hemanthchowdary/Downloads/nexaglobal-project-doc.md`

## Project Intent

NexaGlobal is planned as:

- a multi-region corporate website on AEMaaCS
- US as master blueprint
- UK, FR, DE, and JP regional/localized sites
- editable templates and reusable components
- Content Fragment-powered article content
- search and headless JSON delivery
- region-specific analytics configuration
- Cloud-ready deployment and Dispatcher support

## Local Environment

Verified on this machine:

- Java `11.0.29`
- Maven `3.9.11`
- AEM Project Archetype `56`
- AEMaaCS SDK API `2026.2.24678.20260226T154829Z-260200`

## Modules

The main parts of the project are:

* [core:](core/README.md) Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
* [it.tests:](it.tests/README.md) Java based integration tests
* [ui.apps:](ui.apps/README.md) contains the /apps (and /etc) parts of the project, ie JS&CSS clientlibs, components, and templates
* [ui.content:](ui.content/README.md) contains sample content using the components from the ui.apps
* ui.config: contains runmode specific OSGi configs for the project
* [ui.frontend:](ui.frontend.general/README.md) an optional dedicated front-end build mechanism (Angular, React or general Webpack project)
* [ui.tests:](ui.tests/README.md) Cypress based UI tests (for other frameworks check [aem-test-samples](https://github.com/adobe/aem-test-samples) repository
* all: a single content package that embeds all of the compiled modules (bundles and content packages) including any vendor dependencies

## How to build

To build all modules:

    mvn clean install

To deploy to local author:

    mvn clean install -PautoInstallSinglePackage

To deploy to local publish:

    mvn clean install -PautoInstallSinglePackagePublish

To deploy only the OSGi bundle:

    mvn clean install -PautoInstallBundle

## Suggested Start Sequence

1. Start local AEM author on `4502`
2. Optionally start publish on `4503`
3. From this project root, run:

    mvn clean install -PautoInstallSinglePackage

4. Open:

   - `http://localhost:4502/sites.html`
   - `http://localhost:4502/assets.html`
   - `http://localhost:4502/crx/de`

## Recommended First Implementation Milestone

Implement these first:

- `basepage`
- homepage template
- header
- footer
- hero
- one authored homepage under `/content/nexaglobal/us/en`

After that, move into:

- content page
- article page
- language switcher
- search
- Content Fragments

## Current Notes

The archetype generated a standard AEMaaCS baseline. It still needs NexaGlobal-specific:

- template names and structure
- content roots
- custom component folders
- Sling Models and services
- workflow and analytics setup

The implementation roadmap is tracked in:

- [`docs/NEXAGLOBAL-IMPLEMENTATION-PLAN.md`](docs/NEXAGLOBAL-IMPLEMENTATION-PLAN.md)

## Alternative Deploy Example

    mvn clean install -PautoInstallSinglePackage -Daem.port=4503

To deploy a single content package from a module directory:

    mvn clean install -PautoInstallPackage

## Documentation

The build process also generates documentation in the form of README.md files in each module directory for easy reference. Depending on the options you select at build time, the content may be customized to your project.

## Testing

There are three levels of testing contained in the project:

### Unit tests

This show-cases classic unit testing of the code contained in the bundle. To
test, execute:

    mvn clean test

### Integration tests

This allows running integration tests that exercise the capabilities of AEM via
HTTP calls to its API. To run the integration tests, run:

    mvn clean verify -Plocal

Test classes must be saved in the `src/main/java` directory (or any of its
subdirectories), and must be contained in files matching the pattern `*IT.java`.

The configuration provides sensible defaults for a typical local installation of
AEM. If you want to point the integration tests to different AEM author and
publish instances, you can use the following system properties via Maven's `-D`
flag.

| Property              | Description                                         | Default value           |
|-----------------------|-----------------------------------------------------|-------------------------|
| `it.author.url`       | URL of the author instance                          | `http://localhost:4502` |
| `it.author.user`      | Admin user for the author instance                  | `admin`                 |
| `it.author.password`  | Password of the admin user for the author instance  | `admin`                 |
| `it.publish.url`      | URL of the publish instance                         | `http://localhost:4503` |
| `it.publish.user`     | Admin user for the publish instance                 | `admin`                 |
| `it.publish.password` | Password of the admin user for the publish instance | `admin`                 |

The integration tests in this archetype use the [AEM Testing
Clients](https://github.com/adobe/aem-testing-clients) and showcase some
recommended [best
practices](https://github.com/adobe/aem-testing-clients/wiki/Best-practices) to
be put in use when writing integration tests for AEM.

## Static Analysis

The `analyse` module performs static analysis on the project for deploying into AEMaaCS. It is automatically
run when executing

    mvn clean install

from the project root directory. Additional information about this analysis and how to further configure it
can be found here https://github.com/adobe/aemanalyser-maven-plugin

### UI tests

They will test the UI layer of your AEM application using Cypress framework.

Check README file in `ui.tests` module for more details.

Examples of UI tests in different frameworks can be found here: https://github.com/adobe/aem-test-samples

## ClientLibs

The frontend module is made available using an [AEM ClientLib](https://helpx.adobe.com/experience-manager/6-5/sites/developing/using/clientlibs.html). When executing the NPM build script, the app is built and the [`aem-clientlib-generator`](https://github.com/wcm-io-frontend/aem-clientlib-generator) package takes the resulting build output and transforms it into such a ClientLib.

A ClientLib will consist of the following files and directories:

- `css/`: CSS files which can be requested in the HTML
- `css.txt` (tells AEM the order and names of files in `css/` so they can be merged)
- `js/`: JavaScript files which can be requested in the HTML
- `js.txt` (tells AEM the order and names of files in `js/` so they can be merged
- `resources/`: Source maps, non-entrypoint code chunks (resulting from code splitting), static assets (e.g. icons), etc.

## Maven settings

The project comes with the auto-public repository configured. To setup the repository in your Maven settings, refer to:

    http://helpx.adobe.com/experience-manager/kb/SetUpTheAdobeMavenRepository.html
