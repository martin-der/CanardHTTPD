# Canard HTTPD

Canard HTTPD is an android application that allows to share resources using HTTP.


**Warning, this a application is in an alpha state**

It quite works as expected but many functionnality as missing or buggy.

## How ?
It uses [Jetty](http://www.eclipse.org/jetty/1) for all the HTTP hard work.

When a resource is shared via CanardHTTPD, a web server instance is spawn with the resource available for download.

## Requiered permissions

* Internet
* Network State

May be added
* External Storage Read

## Dependencies

* jetty 8.1.18.v20150929
* httpcore 4.0.1
* mdua ([github/mdua](https://github.com/martin-der/mdua ))
