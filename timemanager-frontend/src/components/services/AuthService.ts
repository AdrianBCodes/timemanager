// const API_URL = 'http://localhost:8080/api/v1/auth';

// class AuthService {
//     async login(username: string, password: string) {
//         const requestOptions = {
//             method: "POST",
//             headers: { "Content-Type": "application/json" },
//             body: JSON.stringify({username, password})
//         };
//         const data = await fetch(API_URL + '/signin', requestOptions)
//         if(!data.ok){
//             throw Error('Adding client failed')
//          }
//         data.json()
//           .then(response => {
//             if (response.accessToken) {
//               localStorage.setItem('user', JSON.stringify(response));
//             }
//             return response;
//           });
//   }

//   logout() {
//     localStorage.removeItem('user');
//   }

//   async register(username: string, email: string, password: string) {
//     const requestOptions = {
//         method: "POST",
//         headers: { "Content-Type": "application/json" },
//         body: JSON.stringify({username, email, password})
//     };
//     return await fetch(API_URL + 'signup', requestOptions);
//   }
// }

// export default new AuthService();

import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1/auth/';

class AuthService {
  login(user: any) {
    return axios
      .post(API_URL + 'signin', {
        username: user.username,
        password: user.password
      })
      .then(response => {
        if (response.data.accessToken) {
          localStorage.setItem('user', JSON.stringify(response.data));
        }

        return response.data;
      });
  }

  logout() {
    localStorage.removeItem('user');
  }

  register(user: any) {
    return axios.post(API_URL + 'signup', {
      username: user.username,
      email: user.email,
      password: user.password
    });
  }
}

export default new AuthService();