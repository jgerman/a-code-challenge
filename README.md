# arena-code-challenge

Solution for the Arena Code Challenge.

## Usage

Either from the command line:

```bash
lein run <filename> <threshold>
```

I didn't get fancy with the cli processing, to make things easy the code expects
the filename of a file in the resource path:

```bash
lein run Artist_lists_small.txt 50
```

Alternatively jacking-in to the repl:

```clojure
;; base-file is included as a def for convenience and is bound to the provided file
(run-search base-file 50)
```

Output goes to standard out to be redirected into a file if desired.


## Summary

The threshold value can be adjusted down to around 10 for reasonable performance
with the current data set. If the set were larger or a lower threshold is going
to be provided a different approach might be necessary.

The approach is pretty straightforward. The only "optimization" I made was to
remove artists that appear on fewer lists than the threshold since they can't
meet the critera.

My initial quick cut at the solution just used a `for` list comprehension to get
the cross product of all artists. That worked but required a lot more
post-processing cleanup and made the code more verbose than necessary. I
switched that to a recursive function.

I did choose to use uuids as the set identifiers, which may slow things down
slightly when calculating the intersection of sets of lists.

### A note on tests

Typically my process is repl driven iterative development. For non-trivial
functions I turn those repl calls into a test once I'm satisfied. This went
quickly enough that the process of writing tests seemed like overkill.

Since it's a code test I did include a few tests just to show I know how it's done.

Tests can either be run from the cli as normal:

```bash
lein test
```
