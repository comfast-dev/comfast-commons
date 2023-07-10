# Overview
This repo contains utility classes:
- `dev.comfast.events.*` - Observer implementation. 
- `dev.comfast.experimental.config` - Configuration reader. Read config from JSON/YAML / System properties.
- `dev.comfast.rgx` - Inline API for regular expressions in JAVA with fluent interface.
- `dev.comfast.util.time` - Measure elapsed time and format results.
- `dev.comfast.util.waiter` - Allow to repeat given action till it finish without error or throw Timeout. 
- `dev.comfast.util.TempFile` - Manage/read/write temp file 
- `dev.comfast.util.TerminalGenerator` - Build ASCII tables
- 

## Deployment
Deployment requires 2 things: 
1. Configured GPG with GPG key
2. File with all secrets in `~/.gradle/gradle.properties`:
```properties
signing.keyId=0x1234567 is in result of command: gpg --list-signatures --keyid-format 0xshort
signing.password=<<gpg-password>>
signing.secretKeyRingFile=C:\\users\\<<USER>>\\.gnupg\\secring.gpg

ossrhUsername=user-with-access-to-https://s01.oss.sonatype.org/
ossrhPassword=...
```


## Events
Main class `EventManager` manages publishing events (`EventsNotifier`) and subscribing them (`SubscriberManager`).
Client library need to:
- create/manage instance of `EventsManager`
- call notifyBefore()/notifyAfter()/action() methods 
- instrument its clients to add/remove listeners (use EventsManager instance) (implementation of `EventListener`).

### How to use: 
1. Create instance of EventManager (use Your favourite Dependency Injection, or just static field):
2. Use EventsManager to publish the events (short example):
```java
class SimpleEventEmitter {
    public static final EventsManager myEvents = new EventsManager<>();
    /**
     * Use 'action()' method. It will automatically call notifyBefore() and notifyAfter()
     * Uses null context object
     */
    void myMethod() {
        myEvents.action(new BeforeEvent(null, "myAction"), () -> {
            //do some action
        });
    }
}
```
3. Listen for events in client code: 
```java
import static com.example.SimpleEventEmitter.myEvents;
class SomeClientClass {
    SomeClientClass() {
        myEvents.on("myAction", e -> System.out.println("Event taken " + e.time));
    }
}
```


## Config
See javaDocs



