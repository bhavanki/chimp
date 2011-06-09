CHIMP 1.1 README
----------------

1. What is CHIMP?
2. Installing CHIMP
3. Extra legal stuff
4. Et cetera

1. What is CHIMP?

CHIMP is a program that stores passwords for you. I know I have way too many
passwords to remember, and it's a pain having to keep guessing what they are
when the time comes. CHIMP is like a wallet that holds passwords for you.

Of course, you could just keep your passwords in a text file, but if somebody
were to find that file ... uh oh. CHIMP gives you the option (which I
strongly encourage) to encrypt your password file. You tell CHIMP the password
to use when you save it, and from then on it can only be decrypted with that
password. That is much safer, provided you use a tough-to-guess password.

By the way, CHIMP was born out of my desire to learn more about encryption
and XML, and inspired by one of the student projects at http://mindprod.com.
Go check out the site, it's pretty nice.

2. Installing CHIMP

CHIMP is a Java 2 program, so you need a Java 2 runtime. I recommend version
1.4 or higher of either the Java Development Kit or Runtime Environment. Go
to java.sun.com to download it.

Just execute the chimp.jar JAR file to start CHIMP. On the command line:

java -jar chimp.jar

or double-click on it if Windows if you have JARs associated with javaw. You
can also use the chimp.bat or chimp.sh files.

There is a sample file included in the distribution called sample.chm. You can
open it with the password "sample".

3. Extra legal stuff

The parts of CHIMP that I wrote are under the GNU General Public License
(www.gnu.org). Use / modify / distribute CHIMP subject to the tenets of the
GPL, have fun, make it better, whatever.

Here's an extra disclaimer in as legalese as I can muster.

CHIMP is a utility for storing passwords. CHIMP tries to ensure that the
passwords and other information stored by it cannot be appropriated by third
parties and used in a manner harmful to the rightful owner of the information.
However, there is no guarantee or warranty provided by the author (Bill
Havanki) that this information is indeed safe from third parties. If such
information were to be appropriated from CHIMP by third parties without the
owner's consent, whether through normal operation of the program or by taking
advantage of bugs or security oversights in CHIMP, the author shall not be
held responsible or liable for any harm or loss to the owner of the information
as a result of the third parties' use of that information.

The above paragraph restates in specific terms the statements made in article
12 of the GPL, and does not substitute for that article. The GPL also proffers
this same protection upon all those who modify CHIMP.

For your information, the Java Crytography Extension, which is the code that
actually performs the encryption and decryption that protects the files CHIMP
uses, also contains a similar disclaimer.

So in plain English, CHIMP is not guaranteed to be bulletproof in protecting
your passwords. You should still use good judgment in using CHIMP.

4. Et cetera

Here are some suggestions on the aforementioned good judgment. Sorry if these
seem obvious to some.

* Although CHIMP lets you store your password files unencrypted, you shouldn't
  do that unless you know your files are extremely safe. Use a password; if
  you are using CHIMP, you should be familiar with remembering passwords by
  now. :)
* Use a good password, with numbers and punctuation marks in it, the longer the
  better.
* Don't name your password file something obvious, like "mypasswords.chimp".
  If somebody is hunting for a password file, well, that would be a dead
  giveaway, now wouldn't it. Be as obfuscating as possible; name it like a
  spreadsheet file or something. Camouflage!
* If there are some passwords that you are very protective of, consider NOT
  using CHIMP to store them.

Most times I've gotten passwords, I've been told not to write them down. I'm
rather conscientious, so I don't write them down (well, most of the time ;) ).
Using CHIMP is a lot like writing them down, except that you can (and should)
encrypt them. If your conscience is not eased by the encryption, then please
don't use CHIMP!

Anyway, if you do use it, I hope it is useful for you and works well. It has
been fun creating it. Just remember, everything is better with monkeys!

Bill Havanki
