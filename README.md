tablestream
===========

Java Library for streaming large tables of data.

You will need gradle to build.

How to play around with the command line interface:

```
gradle install
cd build/install/tablestream
chmod +x bin/tablestream
bin/tablestream
```

This will show you the commands and options.

Example Command Lines
=====================

Create a random table:

```
bin/tablestream random test.ts -rows 10000 -cols 4
```

Create a sorted version of the table:

```
bin/tablestream bigsort test.ts
```

Check if sorted:

```
bin/tablestream sorted test.sorted.ts
```

Count the rows:

```
bin/tablestream count test.sorted.ts
```

Roadmap
=======

- 0.2 first internal production release
- 0.3 multi threading in the bigsort join phase

Wishlist
========

- more file formats: xls(x), csv, html


