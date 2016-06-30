# Controllers of the RED FLAGS webapp



Below I list the controllers with their mapped URLs and their description and parameters.



## ChartCtrl (RestController)

Provides JSON data for the charts displayed on the front page.

URL                     | Description
------------------------|------------
`/charts/flagCounts`    | Data for the donut chart
`/chart/flaggedNotices` | Data for the area chart
`/chart/sumValues`      | Data for the bar chart



## FilterCtrl

Handles requests of filter edit pages.

URL                   | Description | Parameters
----------------------|-------------|-----------
`/filter/delete/{id}` | Initiates the deletion of the specified filter then redirects to `/filters` | `id` - path variable, identifier of the filter to be deleted (long)
`GET /filter/{id}`    | Displays the edit page of a filter (view: [filter](/developer/webapp/views/#filter)) | `id` - path variable, identifier of the filter to be displayed (long)
`POST /filter/{id}`   | Initiates modification of the specified filter then redirects to `/filters` | `id` - path variable, identifier of the filter to be modified (long)<br>`name` - display name of the filter (required)<br>`subscribe` - whether user subscribes to this filter (boolean, default: `false`)



## FilterEmailCtrl (RestController)

Receives trigger to send subscription emails.

URL                   | Description | Parameters
----------------------|-------------|-----------
`/send-filter-emails` | Initiates sending of subscription letters and returns with `true` or `false` based on success | `secret` - the trigger secret, see [configuration](/maintainer/webapp/#triggering-subscription-emails) (required)



## FiltersCtrl

Displays the saved filters list.

URL        | Description
-----------|------------
`/filters` | Displays the saved filters list page (view: [filters](/developer/webapp/views/#filters))



## IndexCtrl

Serves the front page and related stuff.

URL           | Description
--------------|------------
`/`           | Displays the front page (view: [index](/developer/webapp/views/#index))
`/*`          | Redirects every non-mapped URLs to `/`
`/robots.txt` | Serves `robots.txt`
`/version`    | Displays version information



## NoticeCtrl

Serves notice pages.

URL            | Description | Parameters
---------------|-------------|-----------
`/notice`      | Redirects to `/notices`
`/notice/`     | Redirects to `/notices`
`/notice/{id}` | Displays notice page for the given notice ID (view: [notice](/developer/webapp/views/#notice)) | `id` - identifier of the notice to be displayed



## NoticesCtrl

Serves notice list pages.

URL                               | Description | Parameters
----------------------------------|-------------|-----------
`/notices`                        | Redirects to `/notices/10/1/by-date`
`/notices/`                       | Redirects to `/notices/10/1/by-date`
`/notices/{count}`                | Redirects to `/notices/{count}/1/by-date`
`/notices/{count}/`               | Redirects to `/notices/{count}/1/by-date`
`/notices/{count}/{page}`         | Redirects to `/notices/{count}/{page}/by-date`
`/notices/{count}/{page}/{order}` | Validates parameters, gathers filtered data, builds model and displays list page (view: [notices](/developer/webapp/views/#notices)) | `contr` - contracting authority filter<br>`cpv` - CPV code filter<br>`date` - publication date filter<br>`doc` - document type filter<br>`filter` - filters in compact string format<br>`flags` - flag count filter<br>`indicators` - indicator filter<br>`saveFilter` - value of *Save filter* button<br>`text` - text filter<br>`value` - total value filter<br>`winner` - winner organizaiton filter



## OrganizationCtrl

Serves organization pages.

URL                  | Description | Parameters
---------------------|-------------|-----------
`/organization`      | Redirects to `/organizations`
`/organization/`     | Redirects to `/organizations`
`/organization/{id}` | Displays organization page for the given organization ID (view: [organization](/developer/webapp/views/#organization)) | `id` - identifier of the organization to be displayed



## OrganizationsCtrl

Serves organization list pages.

URL                             | Description | Parameters
--------------------------------|-------------|-----------
`/organizations`                | Redirects to `/organizations/10/1`
`/organizations/`               | Redirects to `/organizations/10/1`
`/organizations/{count}`        | Redirects to `/organizations/{count}/1`
`/organizations/{count}/`       | Redirects to `/organizations/{count}/1`
`/organizations/{count}/{page}` | Validates parameters, gathers filtered data, builds model and displays list page (view: [organizations](/developer/webapp/views/#organizations)) | `count` - path variable, number of items per page (integer)<br>`page` - path variable, page number (integer)<br>`filter` - organization name [filter](/user/organizations/#filtering-organizations)



## SecurityCtrl

Handles requests related to user accounts.

URL                                 | Description | Parameters
------------------------------------|-------------|-----------
`/activate/{id}/{token}`            | Initiates user account activation then displays login page (view: [login](/developer/webapp/views/#login)) | `id` - path variable, identifier of user to be activated (integer)<br>`token` - path variable, validation token
`GET /change-password/{id}/{token}` | Displays password changer page (view: [change-password](/developer/webapp/views/#change-password)) | `id` - path variable, identifier of user whose password needs to be changed (integer)<br>`token` - path variable, validation token
`POST /change-password`             | Saves new password then displays login page (view: [login](/developer/webapp/views/#login)) | `id` - identifier of user to be modified (required, integer)<br>`token` - validation token (required)<br>`password` - new password to be saved (required)<br>`g-recaptcha-response` - captcha response (required)
`GET /forgot`                       | Displays forgotten password page (view: [login](/developer/webapp/views/#login))
`POST /forgot`                      | Initiates password change then displays forgotten password page (view: [login](/developer/webapp/views/#login)) | `email` - email address of the user who needs new password (required)<br>`g-recaptcha-response` - captcha response (required)
`/login`                            | Displays login page (view: [login](/developer/webapp/views/#login))
`GET /register`                     | Displays registration page (view: [login](/developer/webapp/views/#login))
`POST /register`                    | Initiates registration then displays registration (view: [login](/developer/webapp/views/#login)) | `email` - email address (required)<br>`password` - password (required)<br>`name` - username (required)<br>`g-recaptcha-response` - captcha response (required)