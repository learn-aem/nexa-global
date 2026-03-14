# Local Runbook

## Project Path

- `/Users/hemanthchowdary/Project/nexaglobal`

## Verified Tool Versions

- Java: `11.0.29`
- Maven: `3.9.11`

## Build the Project

From the project root:

```bash
cd /Users/hemanthchowdary/Project/nexaglobal
mvn clean install
```

## Deploy to Local Author

If local AEM author is running on `4502`:

```bash
cd /Users/hemanthchowdary/Project/nexaglobal
mvn clean install -PautoInstallSinglePackage
```

## Deploy to Local Publish

If local AEM publish is running on `4503`:

```bash
cd /Users/hemanthchowdary/Project/nexaglobal
mvn clean install -PautoInstallSinglePackagePublish
```

## Deploy Only the Bundle

```bash
cd /Users/hemanthchowdary/Project/nexaglobal
mvn clean install -PautoInstallBundle
```

## Start Local AEM SDK

Typical local setup:

1. Put the author quickstart jar in a folder such as:
   - `~/Adobe/author`
2. Put the publish quickstart jar in:
   - `~/Adobe/publish`
3. Rename jars if needed, then start:

```bash
cd ~/Adobe/author
java -jar aem-sdk-quickstart-*.jar
```

```bash
cd ~/Adobe/publish
java -jar aem-sdk-quickstart-*.jar -r publish
```

## Run Dispatcher Locally

From the Dispatcher SDK location, point it at the project dispatcher source:

```bash
<dispatcher-sdk>/bin/docker_run.sh validate /Users/hemanthchowdary/Project/nexaglobal/dispatcher/src
```

```bash
<dispatcher-sdk>/bin/docker_run.sh host.docker.internal:4503 /Users/hemanthchowdary/Project/nexaglobal/dispatcher/src
```

## What to Check After First Deploy

Open these URLs:

- `http://localhost:4502/sites.html`
- `http://localhost:4502/assets.html`
- `http://localhost:4502/crx/de`
- `http://localhost:4502/system/console/bundles`
- `http://localhost:4502/system/console/components`

## First Real Milestone

After the project deploys, implement this sequence:

1. Homepage template
2. Base page component
3. Header
4. Footer
5. One US homepage under `/content/nexaglobal/us/en`

## Notes from Initial Build

The first full build on this machine succeeded.

Observed warnings worth knowing:

- archetype baseline package warnings in `ui.apps.structure` and `ui.content`
- outdated `aemanalyser` plugin warning suggesting `1.6.16`
- npm audit warnings in frontend and ui test dependencies

These did not block the build.
