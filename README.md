# chimp

A simple password manager written in Java.

## What is CHIMP?

CHIMP is a program that stores passwords for you, like other password manager
tools like [Password Safe](https://www.schneier.com/passsafe.html) and
[LastPass](https://lastpass.com). In fact, you should probably use one of those
utilities instead of this one. CHIMP is really just a toy project that I
started way back in 2001 and work on for fun. Still, I use it, and it works.

## Building

CHIMP requires Java 7 and Maven to build.

```
mvn package
```

## Running

If you are on a Mac, run Chimp.app.

Otherwise, you will need to have Java 7 installed.

```
java -jar target/chimp-_version_.jar
```

It's an executable JAR, so you can also just double-click it, although on a Mac
the .app is better.

There is a sample file included in the distribution called sample.chm. You can
open it with the password "sample".

## License

CHIMP is licensed under the
[GNU General Public License, version 2](http://opensource.org/licenses/gpl-2.0.php).

See LICENSE.md for license information for components used in CHIMP.

### Extra Legal Disclaimer

Articles 11 and 12 of GPL v2 apply to CHIMP. In plain English: There is no
warranty for CHIMP. The copyright holder assumes no liability or responsibility
for losses and damages if third parties gain access to passwords stored in
CHIMP. Use at your own risk.

## Et cetera

Here are some suggestions on using CHIMP.

* Although CHIMP lets you store your password files unencrypted, you shouldn't
  do that unless you know your files are extremely safe.
* Use a strong password for your password file.
* Don't name your password file something obvious, like "mypasswords.chimp".
* If there are some passwords that you are very protective of, consider _not_
  using CHIMP to store them.
* If you're not sure CHIMP is all that secure, don't use it. :)

If you do use CHIMP, I hope it is useful for you and works well. Just remember, everything is better with monkeys!
