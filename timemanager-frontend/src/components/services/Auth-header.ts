export default function authHeader() {
    const storedUser = localStorage.getItem('user');
    let user = null;
    if(storedUser !== null){
      user = JSON.parse(storedUser);
    }
    
    
    if (user && user.accessToken) {
      return 'Bearer ' + user.accessToken;
    } else {
      return "";
    }
  }