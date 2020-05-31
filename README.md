# Pretty Passwords  
 
All information will be encrypted and stored locally in your device. Therefore the data is never put into some database you don't know where in the internet.  
  
  
#### What make this password manager unique
  
- You can remember passwords however you want as usual, but the underlying real password for each site is strong and different  
  
- Easy sign up with no personal information  
  
- Everything happens locally, only you have control of the content  
  
- No internet connection required  
  
- No database dependencies, can't break the database if there isn't one  
  
- Not even the creator know who who is using this app or who they are  

  **Truly a virtue password vault only you can physically maintain!**  
  
    
#### To steal your password, one must:  
  
1. Physically obtain a copy of your cryptoFile
  and
2. Break libsodium or somehow know your master password and pretty passwords  
  

  
### Challenges  
  
  1.  If all information are encrypted and stored as a single file in disk, how do you update only a part of the content. (Etc If a user wants to edit one password, only the tag that contains the password needs update, all other tags remain unchanged).
  **Resolution:** Break each tag and entry into its own model with reference to each other, when update occur, construct the content from each model and create a new file.
  
1. [Serializable](https://stackoverflow.com/questions/44698863/bundle-putserializable-serializing-reference-not-value) and [parcelable](https://stackoverflow.com/questions/37694110/when-a-parcelable-object-is-passed-through-an-intent-does-it-update-with-refere) objects do not reference the original object before passing into the activity. Instead they create a new object with the same content. Thus the password entry getting modified and the password entry getting saved into the disk are not the same object.
**Resolution:** Instead of passing in the object into the activity, pass in the index of the object, and retrieve the object from new activity.
  
2. List of tags and entries need to update automatically as user add or delete new item.
    **Resolution:** Use a BroadcastReceiver to listen for such events and update the data list inside the RecyclerView's adapter
  
### Android concepts used
  
- [x] SharedPreferences
- [x] Fragments
- [x] RecyclerView with adapter
- [x] Editable Pop Up
- [x] Custom UI class 
- [ ] SideNav Drawer
- [ ] BoardCast Reciever


### Flow

- sdk need encryption, credential, file create, read, write, email, generate password, change password reminder  
  
- app: set up password, created credential, and saved to file  
  
- experiment with UI (pop up and say login failed)  
  
- asynctask for opening file  
  
