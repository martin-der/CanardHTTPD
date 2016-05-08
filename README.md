# Canard HTTPD

Canard HTTPD is an android application that allows to share resources using HTTP.![CanardHTTPD Icon](https://raw.githubusercontent.com/martin-der/CanardHTTPD/master/src/main/res/mipmap-mdpi/canard_httpd_main.png "CanardHTTPD Icon")


**Warning, this application is in an alpha state.**

It quite works as expected but many functionnalities are missing or buggy.

## How ?
It uses [Jetty](http://www.eclipse.org/jetty/) for all the HTTP hard work.

When a resource is shared via CanardHTTPD, a web server instance is spawn with the resource available for download.

## Requiered permissions

* Internet
* Network State

May be added
* External Storage Read

## Dependencies

* jetty 8.1.18.v20150929 (Downloadable [here as a bundle](http://download.eclipse.org/jetty/updates/jetty-bundles-8.x/8.1.18.v20150929/))
* servlet-api-3.0.jar (Here is the [**3.0.1 version** from maven repository](http://mvnrepository.com/artifact/javax.servlet/javax.servlet-api/3.0.1))
* httpcore 4.0.1 (From [Apache archives](http://archive.apache.org/dist/httpcomponents/httpclient/))
* Apache's commons-io 2.5 (From [Apache commons-io page](http://commons.apache.org/proper/commons-io/))
* mdua ([github/mdua](https://github.com/martin-der/mdua))
