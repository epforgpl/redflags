# Browsing notices on the RED FLAGS website



## Using notice list



The [list of notices](http://www.redflags.eu/notices) can be reached from the welcome page or from the header on any page by clicking on the *"Notices"* link.

The list pages have the following structure:

* *list header* - displays count of notices, ordering options, and buttons for using filters.
* *data table* - shows basic information of the notices and provides links to the details.
* *list footer* - here are the buttons for turning pages.



### List header



On the left side of the list header you can see **how many** notices are there in the database. When you are using any filter, the **count of filtered** notices is also shown in front of the total count.

Next to the numbers, there is the *Order* button which helps you switch between the **ordering mode**. Notices can be ordered **by date**, from newest to oldest, and **by flag count**, from most flags to least flags.

On the right there are two buttons. One leads to the **saved filters**, the other one toggles the **filter panel**, which will be described [below](#filtering-notices).



### Data table



The data table consists of the following 4 columns:

* The first column displays the **notice ID** and the **date of publication**. Clicking on the ID will lead to the [notice page](#notice-details).
* The second column containts the following data:
	* **type** of document (bold text in first line)
	* **name** of the contract (II.1.1) if available (italic text in first line) - clicking on it will also lead to the notice page
	* the **contracting authority** if available
	* the **winner organizations** if there's any
* The third column shows the values:
	* the total **estimated value** if available
	* the total **final value** if available
* The fourth column stands for the **flags**. If you move your mouse over a flag, a hint will appear with the name of the indicator which produced that flag. You can read the details of the flag on the notice page. The description of the indicators is available in [this document](#indicators).



### List footer



There are two button groups in this part of page.

With the left one you can **turn the pages**. There are buttons for:

* the previous page
* the first 3 page
* the previous 2 and next 2 pages of the current page
* the last 3 page
* the next page

The right button group stands for the **items per page** setting. You have the following options:

* 5
* 10
* 25
* 100



## Filtering notices



You can search and filter notices on the notice list page, using the *filter panel*. This can be opened by clicking on the *"Filter"* button on the **right side of the list header**.



### Filter types



Currently *Red Flags* provides the following filters (in order of appearance):

* **Document type** - You can filter notices by document type. The most frequent and important ones are *Contract notice* and *Contract award*.
* **Contracting authority** - You can filter notices by name of their contracting authority. You can type a **part of the name** (see [how it works](#text-search-method)) or an **organization ID**.
* **Award winner** - You can filter notices by the name of (one of) their award winner. Works as the same as the previously described filter.
* **CPV code** - You can filter notices by their given CPV codes. You can type more CPV codes separated by comma (<kbd>,</kbd>), each notice in the result will have *all* CPV code you typed.
* **Publication date** - You can filter notices by their publication date. You can type a date range in `yyyy-mm-dd:yyyy-mm-dd` format, but a date range picker will help you.
* **Total value** - You can filter notices by their total estimated or final value. You can type in a range in `number-number` format, where the numbers will be treated as millions.
* **Texts** - You can search in: description, financial and technical conditions, personal situation, award criteria, additional informations with this filter. The expression you type will be handled as mentioned [here](#text-search-method).
* **Flag count** - You can filter notices by how many flags they have. You can type a range in `number-number` format.
* **Flags** - You can select multiple indicators from the list. Each notice in the result will have flags from *all* of the selected indicators.



### Text search method



The search expression you type will be treated as follows:

* the filter will look for your expression inside the texts, so not only at the start of them
* word spaces in your expression will be treated as jokers that match any number of characters
*

For example, if you search for `B C`, it would find:

* `BC`
* `B C`
* `A B C`
* `B C D`
* `A B C D`
* `A B X C D`

But not `C B`.



### Using filters



After you filled in the fields, click on the **Filter button**, to get the results. The *Reset* button will reload the page with all filters turned off.

If you would like to save your filter however, click on the **Save as new filter button**. This will lead you to the edit page of the new filter where you can add a name to it, and choose whether you want to **subscribe** to its results. Subscribing to a filter means you will receive emails when new notices match your filter.



## Handling subscriptions



You can list your saved filters by clicking on the *Saved filters* link on the right side of the list header. You will see a table with the following items in each row:

* the **name** of your filter
* the **description** of your filter auto-generated by *Red Flags* based on your filter settings
* a **filter button**
* an **edit button**
* and a **delete button**

You will see a paper plane icon (<span class="glyphicon glyphicon-send"></span>) besides the name of filters you were subscribed to.

You can subscribe to or unsubscribe from a filter using the **edit** option and checking or unchecking the tickbox.



## Notice details



When you click on a notice link, you will get to its details page. Here you can see the following panels:

* **Document family** - All available notices of the current public procurement with their ID, document type and flags. Just like in the notice list, if you move your mouse over a flag, the name of the indicator will appear.
* **Flags** - The flags of the current document with justification. There's an info sign (<span class="glyphicon glyphicon-info-sign"></span>) on the right for each flag. Click on this sign to display the description of the indicator.
* **Data** - Metadata of the current notice. It's a very similar table as on the *Data* tab on *TED*. It has a bit fewer rows, but adds winners as well.

Below them, there are panels for each chapter that appear in the notice:

* **I. Contracting authority**
* **II. Object of the contract**
* **Lots**
* **III. Legal, economical, financial, technical information**
* **IV. Procedure**
* **V. Awards**
* **VI. Complementary information**

These don't contain every information from the notice but only the important ones.

URL-s and email addresses in the text will be transformed to clickable links. Organization names in panels I. and V. will take you to the appropriate [organization page](#organization-details).

Each panel (excluding the very first two) can be turned on and off by clicking on its title.
