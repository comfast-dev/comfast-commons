# testkit

## Rgx Kit library
Simplifies every RegExp usage. With this lib every RegExp usage can be done in one line with ease interface. Including groups and error handling. See usecases:

### Examples

#### Simple find
```java
//with rgx kit
String found = rgx(".ab").match("ab ab ab").get();

//pure java
Matcher matcher = Pattern.compile(".ab").matcher("ab ab ab");
if(!matcher.find()) {
    throw new RuntimeException("Not found pattern " + ".ab" + "...");
}
String found = matcher.group(0);
```

#### Find all
```java
//with rgx kit
List<String> found = rgx("ab").matchAllAsString("ab ab ab");

//pure java
Matcher matcher = Pattern.compile("ab").matcher("ab ab ab");
List<String> found = new ArrayList<>();
while(matcher.find()) {
    found.add(matcher.group(0));
}
```

#### Find groups
```java
    RgxMatch match = rgx("(.)b").match("ab cb db");
    match.group(0) // -> ab
    match.group(1) // -> a

    List<RgxMatch> matches = rgx("(.)b").matchAll("ab cb db");
    matches.get(0).group(0) // -> ab
    matches.get(0).group(1) // -> a
    matches.get(1).group(0) // -> cb
    matches.get(1).group(1) // -> c
    ...
```


#### Handling errors / empty's
```java
    RgxMatch match = rgx("xxx").match("some text");
    match.isPresent() // -> false
    match.get() // throw "Not found pattern 'xxx' in text: 'some text'"
    match.throwIfEmpty("My error message") // throw "My error message"
    ...
```