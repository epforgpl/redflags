# TED Interface in RED FLAGS engine



## TED Interface overview

It is basically an easy-to-use, high-level downloader (HTTP client) library for *TED* with a lot of configuration options. It provides synchronized methods to download one tab of a notice in the specified display language. It can retry requests and sleep before every download.

The most important methods of `TedInterface`:

```java
TedResponse requestNoticeTabQuietly         ( NoticeID id, DisplayLanguage lang, Tab tab )
TedResponse requestNoticeTab                ( NoticeID id, DisplayLanguage lang, Tab tab ) throws TedError
TedResponse requestNoticeTabWithoutRetrying ( NoticeID id, DisplayLanguage lang, Tab tab ) throws TedError
TedInterfaceConf getConf()
```

"Quietly" means it will not throw any exception but return with `null` instead if any error occurs.

The `TedInterface` class lives in the `hu.petabyte.redflags.tedintf` package. It's constructed to be used as a singleton to avoid parallel downloads which lead to IP banning on *TED*. You can get the instance using the `getInstance()` static method.

There is also a `setInstance(TedInterface)` method so you can replace the singleton instance with another implementation.

*Red Flags* does this, it uses `CachedTedInterface` with `GzippedNoticeCache` in order to save/fetch HTML files into/from a cache directory.

There is a class called `TedInterfaceHolder` which is a *Spring* component providing getter/setter methods for the *TedInterface* instance. `TedInterfaceHolder` is initialized by `RedflagsEngineBoot`.



## Caching

`CachedTedInterface` extends `TedInterface` and adds a caching layer on top of the HTTP client mechanism. When you call `requestNoticeTab`, it will check the cache first and if it finds the requested HTML, returns it. Otherwise the downloading will be performed, and the HTML will be written in a file.

Files will be stored inside the cache directory which is one of the constructor arguments. The other arguments are two `NoticeCache` objects. First one is "old cache", second one is "cache". `CachedTedInterface` designed to be able to move on from and old cache to a new one on-the-fly. When a notice tab is requested, `CachedTedInterface` will check the old cache first (if specified), and if it finds the HTML, it will move it to the new cache.

Methods of `NoticeCache`:

```java
String fetch(NoticeID id, DisplayLanguage lang, Tab tab);
void remove(NoticeID id, DisplayLanguage lang, Tab tab);
boolean store(NoticeID id, DisplayLanguage lang, Tab tab, String raw);
```

Implementations of `NoticeCache`:

* `SimpleNoticeCache` - Stores the HTML files as they are.
* `GzippedNoticeCache` - Stores the HTML files GZipped.

Both of them are implementing `FilesystemNoticeCache` abstract class and both of them use the cache directory structure: `cache/year/number/tab-file`.



## Configuration (TODO)


## Error handling

When the `TedInterface` encounters an error, it will throw a `TedError` exception. These contain a `TedErrorType` object which provides some information about the error and whether it's recoverable or not.

Error types:

Enum value        | Can&nbsp;retry help? | Can&nbsp;crawling be&nbsp;continued? | Description
------------------|-----------------|----------------------------|-------------
`IO_EXCEPTION`    | yes | yes | Possibly a network error - you can retry the request.
`HTTP_ERROR`      | yes | yes | *TED* responded with HTTP error, maybe there is a server error - you can retry the request maybe a bit later.
`INCOMPLETE_FILE` | yes | yes | The response body is bigger than we expected - you need to increase `maxBodySize`.
`INVALID_UDL`     | no  | yes | The requested NoticeID does not exist on TED, maybe it had been deleted.
`EXPIRED_UDL`     | no  | yes | The requested NoticeID is no more available on TED, it is too old.
`UNAVAILABLE_UDL` | yes | yes | The requested NoticeID is not yet available, try again later.
`TEMPORARY_BAN`   | no  | no  | You've just got an IP based temporary ban for 24 hours. Wait 24 hours, increase time between requests and try again.
`MISSING_CONTENT` | yes | yes | The received page doesn't contain some important thing, maybe we have been redirected or there is a server bug on *TED*.
`PARSE_ERROR`     | yes | yes | Failed to parse the HTML code.



## MaxNumberDeterminer (TODO)

used by scopes not tedintf
