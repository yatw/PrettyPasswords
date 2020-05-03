
# Pretty Passwords

All information will be encrypted and stored locally in your device. Therefore the data is never put into some database you don't know where in the internet.

**Truly a virtue password vault only you can physically maintain!**

## About the project



-   sdk need encryption, credential, file create, read, write, email, generate password, change password reminder

-   app: set up password, created credential, and saved to file

-   experiment with UI (pop up and say login failed)

-   asynctask for opening file




flow:

-   user first launch app, shared preference is empty && cryptofile is empty, prompt to new user page, user provide master password, generate pk, sk, esk, and besak, and saved in file

-   returning user, prompt for master password, get sk from esk, use sk and pk and get sak, decrypt whole file

-   to add a password pair, system generated a rp and psk, user provide pretty password, hased pp encrypt psk into epsk, psk encrypt rp, get erp, saved in file

-   to decrypt a password pair, user provide pretty password, use hased pp decrypt epsk, to get psk, use to decrypt




user has to remember master password, and pretty passwords, remember where and how the copy is saved.



to steal your password, one must:

1.  Physically obtain a copy of your cryptoFile

2.  Break libsodium or somehow know your master password and pretty passwords




benefit:

-   you remember passwords however you want as usual, but the underlying real password for each site is strong and different

-   Easy sign up with no personally information

-   Everything happens locally, only you have control of the content

-   no internet connection required

-   No database dependencies, can't break the database if there isn't one

-   I don't know who you are or who used this app

-   what you save, what you get
