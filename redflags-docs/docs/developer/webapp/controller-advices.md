# ControllerAdvice beans in RED FLAGS webapp

Below I list the `ControllerAdvice` beans and the model attributes they add.



## LangAdvice

Model attribute | Description
----------------|------------
`lang`          | Value returned by `getLanguage()` of the current `Locale` object
`language`      | Value returned by `getDisplayLanguage()` of the current `Locale` object
`languages`     | Value of `site.languages` property split by `,`. Default value: `en,hu`



## SiteAdvice

Model attribute | Description
----------------|------------
`message`       | Value of `site.message` property. Default value: empty string
`printCaptcha`  | Value of `site.useCaptcha` property, injected in `SecuritySvc.java`. Default value: `true`



## VersionAdvice

Model attribute | Description
----------------|------------
`appTitle`      | Value of `app.title` property defined in `version.properties`, injected in `VersionConfig.java`
`built`         | Timestamp of the application start (yeah, it's not the build time but it's usually close :))
`version`       | Value of `app.version` property defined in `version.properties`, injected in `VersionConfig.java`


