# Services in RED FLAGS webapp



All service class is in `hu.petabyte.redflags.web.svc` package.



## AccountSvc

Method | Description
-------|------------
`List<Account> findAll()`              | Returns all user accounts from the database.
`long getCurrentUserId()`              | Returns the ID of the currently logged in user or `0` if there's no logged in user in the current session.
`Account getCurrent()`                 | Returns the currently logged in user account or `null` if there's no logged in user in the current session.
`saveAccountAndCryptPassword(Account)` | Saves the given account into the database but encodes the password in `cryptedPassword` field first



## CaptchaValidatorSvc

Implements the only one method of `CaptchaValidator` interface.

Method | Description
-------|------------
`boolean validateCaptcha(String response)` | Validates the user's reCaptcha response using the reCaptcha API, returns `true` if response is valid.



## ChartSvc

Provides data for charts displayed on the front page.

Method | Description
-------|------------
`List<Integer> flaggedNoticeCountsPerQuarter()` | Returns the number of flagged notices by quarter.
`FlagCounts getResult()`                        | Returns data for the donut chart: flag counts and their frequencies.
`List<Integer> noticeCountsPerQuarter()`        | Returns the number of notices by quarter.
`List<String> quarters()`                       | Returns the quarters.
`String sumValueCSV()`                          | Returns data in CSV format for the bar plot.



## EmailSvc

Utility component to send emails.

Method | Description
-------|------------
`boolean send(String to, String subject, String text)` | Sends an UTF-8 encoded, HTML email with the given parameters. Sender will be the value of `redflags.from` property.
<pre>String text(String templateName, Map<String, Object> model<br>HttpServletRequest, HttpServletResponse, Locale)</pre> | Renders the text using the given template file name, model and locale. Rendering is done by `InternalViewRenderer` (also in `svc` package).



## FilterEmailSvc

This component is responsible for sending subscription letters.

Method | Description
-------|------------
`boolean sendFilterLetters(HttpServletRequest, HttpServletResponse)` | Goes through subscribed filters, removes `date` filter (irrelevant), adds internal date filter (to avoid sending already sent notice links), and sends email to the user which contains the freshest 10 notices matching the filter.



## FilterSvc

Method | Description
-------|------------
`delete(long)`                 | Deletes the user filter of the given ID.
`fill(UserFilter)`             | Calls `fillOrgNames` method to add organization names into `info` fields of relevant filters.
`fillLastNoticeId(UserFilter)` | Stores the number and year of the last sent notice into the user filter object - to avoid resending notices next time.
`fillOrgNames(List<Filter>)`   | Adds organization names into `info` fields of relevant filters (`contr` and `winner`) if they contain organization IDs.
`UserFilter get(long)`         | Returns the user filter of the given ID.
`long saveNew(long userId, String filter)` | Stores the new user filter and returns its ID.
`boolean saveSettings(long id, String name, boolean subscribe)` | Saves settings of a user filter and returns `true` on success.



## FiltersSvc

Method | Description
-------|------------
`List<UserFilter> getFilters()` | Returns list of current user's filters



## IndicatorsSvc

Method | Description
-------|------------
`List<String> getIndicators()` | Returns the list of indicator IDs - stored in a list.
`refresh()`                    | Updates the list upon initialization and then every 60 minutes.



## NoticeSvc

Method | Description
-------|------------
`List<Map<String,Object>> awards(String id)` | Returns the awards extended with winners for the given notice ID.
`Map<String,Object> basic(String id)`        | Returns the basic notice information from `rfwl_notices` table for the given notice ID.
`List<Map<String,Object>> cnFlags(String id)`| Returns flags of the contract notice related to the notice identified by `id`.
`Map<String,Object> compl(String id)`        | Returns the complementary information for the given notice ID.
`Map<String,Object> contr(String id)`        | Returns the contracting authority information for the given notice ID.
`Map<String,Object> data(String id)`         | Returns the *Data tab* information for the given notice ID. The result is extended with CPV code descriptions and *Type* names.
`List<Map<String,Object>> flags(String id)`  | Returns flags for the given notice ID.
`Map<String,Object> left(String id)`         | Returns the "LEFT" information (see data documentation) for the given notice ID.
`List<Map<String, Object>> lots(String id)`  | Returns information of lots for the given notice ID, extended with the duration, and CPV code descriptions
`List<Map<String, Object>> objs(String id)`  | Returns *object of the contract* chapters' data for the given notice ID.
`Map<String,Object> proc(String id)`         | Returns the procedure information for the given notice ID.
`List<Map<String, Object>> related(String id)` | Returns basic information of document family member notices for the notice identified by `id`.
`List<Map<String,Object>> winner(String id)` | Returns the ID and name of winners for the given notice ID.



## NoticesSvc

Method | Description
-------|------------
`long count(List<Filter>)`            | Queries the count of notices which match the given filters.
`List<Map<String,Object>> docTypes()` | Queries available document type IDs from the database.
`init()`                              | Initializes *Freemarker* and loads the template for SQL queries: `queries/notices-sql.ftl`.
`Map<String,Object> lastNotice()`     | Queries the freshest notice number and year.
<pre>List<Map<String,Object>> query(int perPage,<br>int offset, boolean byFlags, List<Filter>)</pre> | Queries notices with the given parameters using `sql` method.
`String sql(List<Filter> f, boolean count, boolean byFlags)` | Returns the appropriate query for the given filters. Sets ordering according to the 3rd argument. If `count` is `true`, it creates a `SELECT COUNT(1)...` query. All these paramterers are handled in the template.



## OrganizationSvc

Method | Description
-------|------------
`Map<String,Object> basic(String id)`       | Returns the basic organization information from `te_organization` table for the given organization ID.
`long callCount(String id)`                 | Returns the number of contract notices where the contracting organization ID is `id`.
`List<Map<String,Object>> calls(String id)` | Returns basic information of notices where the contracting organization ID is `id`.
`init()`                                    | Loads the SQL code for the query (`queries/organization-wins.txt`) which returns wins of an organization.
`long winCount(String id)`                  | Returns number of contract award notices where `id` appears at least in one award as winner.
`List<Map<String,Object>> wins(String id)`  | Returns basic information of notices where `id` appears at leat in one award as winner.


## OrganizationsSvc

Method | Description
-------|------------
`String addFilter(String sql, String filter)` | Puts filter into the given SQL query.
`long count(String f)`                        | Returns the count of organizations matching the given name filter.
`init()`                                      | Loads SQL codes for queries (`queries/organizations-query.txt` and `queries/organizations-count.txt`).
<pre>List<Map<String,Object>> query(int perPage,<br>long offset, String filter)</pre> | Returns data of organizations using the given parameters.



## SecuritySvc

Method | Description
-------|------------
`boolean activate(long id)` | Activates the given user account and returns `true` on success.
`boolean changePassword(long id, String pw)` | Saves new password (with encryption) for the given user account.
`String initiatePasswordChange(String email, String urlFormat, Locale l)` | Calls `sendEmailConfirmation` and returns with a message property key which indicates success or error type.
<pre>String initiateRegistration(String email,<br>String pw, String name, String urlFormat, Locale l)</pre> | Saves new account then calls `sendEmailConfirmation` and returns with a message property key which indicates success or error type.
<pre>`String sendEmailConfirmation(Account a,<br>String urlFormat, String labelPrefix, Locale l)</pre> | Saves remember token and expire date for the account then sends email with confirmation link using the parameters. Returns with `labelPrefix` + key of message which describes success or error type.
`boolean validateCaptcha(String r)` | Calls `CaptchaValidatorSvc`, but returns constant `true` if `site.useCaptcha` property is set to `false`.
`boolean validateToken(long id, String token)` | Validates the given token. Returns `false` if the token doesn't belongs to given user or it has been expired.