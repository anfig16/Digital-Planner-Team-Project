let slideIndex = 0;
showSlides();

function showSlides() {
  let i;
  let slides = document.getElementsByClassName("mySlides");
  let dots = document.getElementsByClassName("dot");

  // Hide all slides
  for (i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";
  }

  // Move to next slide
  slideIndex++;
  if (slideIndex > slides.length) {
    slideIndex = 1;
  }

  // Remove "active" class from all dots
  for (i = 0; i < dots.length; i++) {
    dots[i].className = dots[i].className.replace(" active", "");
  }

  // Show current slide
  slides[slideIndex - 1].style.display = "block";

  // Mark current dot as active
  dots[slideIndex - 1].className += " active";

  // Wait 2 seconds, then show next slide
  setTimeout(showSlides, 5000);
}


//Toggle between signup and login forms
const signupBtn = document.getElementById("signup-slider");
const loginBtn = document.getElementById("login-slider");
const signupForm = document.getElementById("form-signup");
const loginForm  = document.getElementById("form-login");

//Default view is signup form
signupForm.style.display = 'block';

//Toggle to signup form when Sign Up button clicked
signupBtn.addEventListener('click', (e) => {
	//Stop page from reloading
	e.preventDefault();
	signupForm.style.display = 'block';
	loginForm.style.display = 'none';
});

//Toggle to log in form when Log In button clicked
loginBtn.addEventListener('click', (e) => {
	//Stop page from reloading
	e.preventDefault();
	signupForm.style.display = 'none';
	loginForm.style.display = 'block';
});

//Send user signup info to database once sign up button is clicked
document.getElementById('signup-button').addEventListener('click', async (e) => {
	//Stop page from reloading
	e.preventDefault();
	
	//Save user input as an object
	const userData = {
	    name: document.getElementById('name').value,
	    email: document.getElementById('email').value,
	    password: document.getElementById('password').value
	  };
	
	//Fetch API- save user input to backend (MongoDB database)
	const response = await fetch('/users/createUser', {
	      method: 'POST',
	      headers: {
	        'Content-Type': 'application/json',
	      },
		  credentials: 'include',
	      body: JSON.stringify(userData)
	});
	
	//Get text response
	const message = await response.text(); 
	
	if (response.ok) {
		  window.location.href = 'planner.html';
		} else {
		  document.getElementById('error-message').textContent = message;
		}
});

//Verify login info from database
document.getElementById('login-button').addEventListener('click', async (e) => {
	//Stop page from reloading
	e.preventDefault();
	
	//Save user input
	const email = document.getElementById('email-login').value;
	const password = document.getElementById('password-login').value;
	
	
	//Login process
	const response = await fetch('/users/login', {
	  method: 'POST',
	  headers: { 'Content-Type': 'application/json' },
	  credentials: 'include',
	  body: JSON.stringify({ email: email, password: password })
	});

	const message = await response.text(); 
	
	if (response.ok) {
	  window.location.href = 'planner.html';
	} else {
	  document.getElementById('error-message').textContent = message;
	}
	
});

//When loading the signup/login page, check for any error messages to display
document.addEventListener('DOMContentLoaded', async () => {
  const params = new URLSearchParams(window.location.search);
  const message = params.get('message');
  const error = document.getElementById('error-message');

  if (message) {
    if (error) {
      error.textContent = message;
    }
  }

  //Clean up the URL
  if (window.history.replaceState) {
	const cleanUrl = window.location.pathname;
    window.history.replaceState({}, document.title, cleanUrl);
  }
  
  	const result = await fetch('/users/checkSession', {
  		credentials: 'include'
  	});
  	const data = await result.json();
  	
  	if(data.loggedIn) {
  		error.textContent = "You are already logged in"
  	}
});