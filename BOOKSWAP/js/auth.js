document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.querySelector("#login-form");
  const registerForm = document.querySelector("#register-form");
  const showMessage = (form, message, success = false) => {
    const element = form.querySelector(".form-message");
    element.textContent = message;
    element.style.color = success ? "#59805b" : "var(--ruby)";
  };
  loginForm?.addEventListener("submit", async (event) => {
    event.preventDefault();
    const form = new FormData(loginForm);
    const response = await BookSwap.request("/api/auth/login", {
      method: "POST",
      body: JSON.stringify(Object.fromEntries(form)),
    });
    if (response) {
      localStorage.setItem("bookswap-user", JSON.stringify(response));
      window.location.href = "dashboard.html";
      return;
    }
    localStorage.setItem(
      "bookswap-user",
      JSON.stringify({
        name: "Alex Student",
        email: form.get("email"),
        college: "Your College",
      }),
    );
    showMessage(loginForm, "Welcome back. Opening your shelf...", true);
    setTimeout(() => (window.location.href = "dashboard.html"), 500);
  });
  registerForm?.addEventListener("submit", async (event) => {
    event.preventDefault();
    const form = new FormData(registerForm);
    if (form.get("password") !== form.get("confirmPassword")) {
      showMessage(registerForm, "Passwords do not match.");
      return;
    }
    const user = Object.fromEntries(form);
    delete user.confirmPassword;
    const response = await BookSwap.request("/api/auth/register", {
      method: "POST",
      body: JSON.stringify(user),
    });
    const session = response || {
      name: user.name,
      email: user.email,
      college: user.college,
    };
    localStorage.setItem("bookswap-user", JSON.stringify(session));
    showMessage(registerForm, "Account created. Welcome to BOOKSWAP.", true);
    setTimeout(() => (window.location.href = "dashboard.html"), 650);
  });
});
