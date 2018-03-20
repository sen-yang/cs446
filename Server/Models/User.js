const Rank = requires(./Rank);

module.exports = class User{
  constructor(UID,username, password, email, image){
    this.UID = UID;
    this.username = username;
    this.password = password;
    this.email = email;
    this.image = image;
    this.Rank = new Rank();
  }
};
