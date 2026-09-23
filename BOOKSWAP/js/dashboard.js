document.addEventListener("DOMContentLoaded", async () => {
  const user = JSON.parse(localStorage.getItem("bookswap-user") || "null");
  if (user) {
    document.querySelector("#welcome-name").textContent =
      (user.name || "reader").split(" ")[0] + ".";
    document.querySelector("#profile-name").textContent =
      user.name || "Alex Student";
  }
  const remote = await BookSwap.request("/api/books/recommended");
  const books = remote || BookSwap.books;
  document.querySelector("#total-books").textContent = books.length + 18;
  document.querySelector("#available-books").textContent = books.length + 12;
  document.querySelector("#recommended-books").innerHTML = books
    .slice(0, 4)
    .map(bookCard)
    .join("");
});
