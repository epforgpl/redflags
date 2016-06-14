# Views in the RED FLAGS webapp



Views are *FreeMarker* templates and they are located in `src/main/resources/templates`. Below I list them with their model attributes.



## Globals

`header.ftl` and `footer.ftl` are included in every page and they use the following global attributes:

Model&nbsp;attribute       | Type       | Description
---------------------------|------------|------------
`appTitle`                 | `String`   | Title of the application
`lang`                     | `String`   | Language code
`languages`                | `String[]` | Available language codes
`needDateRangePicker`      | `Object`   | *(Optional)* If specified and not null, *DateRange Picker* script tag will be included
`needHiCharts`             | `Object`   | *(Optional)* If specified and not null, *HighCharts*, *OwlCarousel* and related resources used on the front page will be included
`needReCaptcha`            | `Object`   | *(Optional)* If specified and not null, *Google reCaptcha* script tag will be included
`pageTitle`                | `String`   | *(Optional)* Title of the page. It will be shown in the window header as `"pageTitle - appTitle"`
`pageTitleLabel`           | `String`   | *(Optional)* Message property to be used as page title. This attribute sets and overrides the value of `pageTitle`
`prevPageTitleLabel`       | `String`   | *(Optional)* Message property to be used as the title of the previous page
`prevPageUrl`              | `String`   | *(Optional)* URL of the previous page
`siteMessage`              | `String`   | *(Optional)* Message property to be displayed in a `well` above content



## change-password

Displays the password changer form if the token is valid. Otherwise it displays a message.

Model&nbsp;attribute | Type      | Description
---------------------|-----------|------------
`email`              | `String`  | E-mail address
`id`                 | `String`  | Account ID
`messageLabel`       | `String`  | *(Optional)* Message property key
`messageLevel`       | `Integer` | *(Optional)* Indicates type of message, modifies the `text-*` CSS class on the message text:<br>`0` - `text-primary`<br>negative number - `text-danger`<br>positive number - `text-success`
`name`               | `String`  | Username
`token`              | `String`  | Change password token



## error

Displays the *Spring* error properties if any exception occurs.



## filter

Displays the edit page for a filter.

Model&nbsp;attribute | Type         | Description
---------------------|--------------|------------
`filter`             | `UserFilter` | The filter to be displayed and edited



## filters

Displays the list of the user's filters.

Model&nbsp;attribute  | Type               | Description
----------------------|--------------------|------------
`filters`             | `List<UserFilter>` | List of the user's filters



## index

Displays the front page. The embedded video and the about text depends on the `lang` attribute.

Model&nbsp;attribute       | Type       | Description
---------------------------|------------|------------
`lang`                     | `String`   | Language code



## list-error

Displays an error message. This page is used by `NoticesCtrl` and `OrganizationsCtrl` when querying fails.



## login

Displays the login, forgotten password and registration form.

Model&nbsp;attribute | Type      | Description
---------------------|-----------|------------
`messageLabel`       | `String`  | *(Optional)* Message property key
`messageLevel`       | `Integer` | *(Optional)* Indicates type of message, modifies the `text-*` CSS class on the message text:<br>`0` - `text-primary`<br>negative number - `text-danger`<br>positive number - `text-success`
`mode`               | `String`  | *(Optional)* Values can be: `login` (default), `forgot`, and `register`. This attribute tells which of the accordion sections will open initially



## notice

Displays the detail page of a notice. The attributes mostly refer to the fields of the `Notice` object although queries are not mapped to that POJO.

Model&nbsp;attribute | Type                       | Description
---------------------|----------------------------|------------
`awards`             | `List<Map<String,Object>>` | *(Optional)* Awards
`basic`              | `Map<String,Object>`       | Basic (most important) information
`contr`              | `Map<String,Object>`       | *(Optional)* Contracting authority
`compl`              | `Map<String,Object>`       | *(Optional)* Complementary information
`data`               | `Map<String,Object>`       | *(Optional)* Data tab
`flags`              | `List<Map<String,Object>>` | *(Optional)* Flags
`cnFlags`            | `List<Map<String,Object>>` | *(Optional)* Flags of the related contract notice
`left`               | `Map<String,Object>`       | *(Optional)* Legal, economical, financial and technical information
`lots`               | `List<Map<String,Object>>` | *(Optional)* Lots
`objs`               | `List<Map<String,Object>>` | *(Optional)* Object of the contract chapters
`procedure`          | `Map<String,Object>`       | *(Optional)* Procedure



## notices

Displays the filtered list of notices.

Model&nbsp;attribute | Type                       | Description
---------------------|----------------------------|------------
`allCount`           | `Long`                     | Count of notices in the database
`counts`             | `List<Integer>`            | Result per page options defined in `NoticesCtrl`
`docTypes`           | `List<Map<String,Object>>` | List of document type ID-s (at `id` key) that have at least one notice in the database
`filter`             | `String`                   | *(Optional)* Current filters as a single string
`filteredCount`      | `Long`                     | Count of notices in the result set
`filters`            | `Map<String,String>`       | *(Optional)* Current filters as string pairs
`filtersList`        | `List<Filter>`             | *(Optional)* Current filters as `Filter` objects
`indicators`         | `List<String>`             | List of indicator ID-s
`objs`               | `List<Map<String,Object>>` | Notices of the current page
`order`              | `String`                   | Order mode, value can be `by-date` or `by-flags`
`page`               | `Integer`                  | Current page number (1-based)
`pages`              | `Integer`                  | Page count
`queryTime`          | `Long`                     | Query time in milliseconds



## organization

Displays the detail page of an organization.

Model&nbsp;attribute | Type                       | Description
---------------------|----------------------------|------------
`basic`              | `Map<String, Object>`      | Basic (most important) information
`callCount`          | `Long`                     | Count of contract notices where this organization was the contracting authority
`calls`              | `List<Map<String,Object>>` | *(Optional)* List of top 10 freshest notices where this organization was the contracting authority
`winCount`           | `Long`                     | Count of contract award notices where this organization appeared as award winner
`wins`               | `List<Map<String,Object>>` | *(Optional)* List of top 10 freshest notices where this organization appeared as award winner



## organizations

Displays the filtered list of organizations.

Model&nbsp;attribute | Type                       | Description
---------------------|----------------------------|------------
`allCount`           | `Long`                     | Count of organizations in the database
`counts`             | `List<Integer>`            | Result per page options defined in `OrganizationsCtrl`
`filter`             | `String`                   | *(Optional)* Organization name filter
`filteredCount`      | `Long`                     | Count of organizations in the result set
`objs`               | `List<Map<String,Object>>` | Organizations of the current page
`page`               | `Integer`                  | Current page number (1-based)
`pages`              | `Integer`                  | Page count
`queryTime`          | `Long`                     | Query time in milliseconds