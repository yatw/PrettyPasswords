
# Pretty Passwords    
100% Secure: All information will be encrypted and stored locally in your device. 
  
100% Persistent: User can export the encrypted file from app to transfer between devices.  

The data is never put into some database you don't know where in the internet.
    
#### What make this password manager unique  
- You can remember passwords however you want as usual, but the underlying real password for each site is strong and different    
    
- No database dependencies, can't break the database if there isn't one    
    
- No internet connection required, only you have access the content
    
- Easy sign up with no personal information    

- Not even the creator know who is using this app or who they are    
     
#### To steal your password, one must:    
1. Physically obtain a copy of your cryptoFile
  and    
2. Break libsodium or somehow know your master password and pretty passwords    
    
  
    
### Issues/Challenges    
 1. If all information are encrypted and stored as a single file in disk, how do you update only a part of the content. (Etc If a user wants to edit one password, only the tag that contains the password needs update, all other tags remain unchanged).    
**Resolution:** Break each tag and entry into its own model with reference to each other, when update occur, construct the content from each model and create a new file.  
    
2. [Serializable](https://stackoverflow.com/questions/44698863/bundle-putserializable-serializing-reference-not-value) and [parcelable](https://stackoverflow.com/questions/37694110/when-a-parcelable-object-is-passed-through-an-intent-does-it-update-with-refere) objects do not reference the original object before passing into the activity. Instead they create a new object with the same content. Thus the password entry getting modified and the password entry getting saved into the disk are not the same object.    
**Resolution:** Instead of passing in the object into the activity, pass in the index of the object, and retrieve the object from new activity.    
  
3. List of tags and entries need to update automatically as user add or delete new item.    
**Resolution:** Use a BroadcastReceiver to listen for such events and update the data list inside the RecyclerView's adapter    
  
4. On softkeyboard hide and show, adjustResize and adjustPan both cause recyclverView to automatically updated its element on the UI without specifically calling notifyDataSetChanged.  
    
### Android concepts used  
- [x] SharedPreferences  
- [x] Fragments  
- [x] RecyclerView with adapter  
- [x] Editable Pop Up  
- [x] Custom UI class   
- [x] BoardCast Receiver  
- [ ] SideNav Drawer  
  
  
### Flow  
  
- sdk need encryption, credential, file create, read, write, email, generate password, change password reminder    
    
- app: set up password, created credential, and saved to file    
    
- experiment with UI (pop up and say login failed)    
    
- asynctask for opening file
