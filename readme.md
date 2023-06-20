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