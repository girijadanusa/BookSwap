let activeType = "all";
document.addEventListener("DOMContentLoaded", async () => {
  const list = document.querySelector("#book-list");
  if (list) {
    renderBooks();
    document
      .querySelector("#search-form")
      ?.addEventListener("submit", (event) => {
        event.preventDefault();
        renderBooks();
      });
    document.querySelectorAll(".filter-chip").forEach((button) =>
      button.addEventListener("click", () => {
        document
          .querySelectorAll(".filter-chip")
          .forEach((item) => item.classList.remove("active"));
        button.classList.add("active");
        activeType = button.dataset.filter;
        renderBooks();
      }),
    );
  }
  const detail = document.querySelector("#detail-title");
  if (detail) renderDetails();
  const add = document.querySelector("#add-book-form");
  add?.addEventListener("submit", addBook);
});
function renderBooks() {
  const query = (
    document.querySelector("#book-search")?.value || ""
  ).toLowerCase();
  const category = document.querySelector("#category-filter")?.value || "all";
  const filtered = BookSwap.books.filter(
    (book) =>
      (activeType === "all" || book.type === activeType) &&
      (category === "all" || book.category === category) &&
      `${book.title} ${book.author} ${book.owner}`
        .toLowerCase()
        .includes(query),
  );
  document.querySelector("#book-list").innerHTML = filtered.length
    ? filtered.map(bookCard).join("")
    : '<p class="muted">No books match that search yet.</p>';
  document.querySelector("#result-count").textContent =
    `${filtered.length} books nearby`;
}
function renderDetails() {
  const id = new URLSearchParams(location.search).get("id");
  const book =
    BookSwap.books.find((item) => String(item.id) === id) || BookSwap.books[0];
  document.querySelector("#detail-title").textContent = book.title;
  document.querySelector("#detail-author").textContent = book.author;
  document.querySelector("#detail-owner").textContent = book.owner;
  document.querySelector("#detail-category").textContent = book.category;
  document.querySelector("#detail-type").textContent = book.type;
  document.querySelector("#detail-condition").textContent = book.condition;
  document.querySelector("#detail-initial").textContent = book.initial;
  document.querySelector("#detail-cover").className =
    `detail-cover ${book.art}`;
  document.querySelector("#request-book").href = `exchange.html?id=${book.id}`;
  localStorage.setItem("bookswap-selected-book", JSON.stringify(book));
}
async function addBook(event) {
  event.preventDefault();
  const form = new FormData(event.currentTarget);
  const user = JSON.parse(localStorage.getItem("bookswap-user") || "{}");
  const payload = {
    ...Object.fromEntries(form),
    ownerId: user.id || "guest",
    ownerName: user.name || "Alex Student",
    country: "India",
    state: user.state || "",
    college: user.college || "",
  };
  const remote = await BookSwap.request("/api/books", {
    method: "POST",
    body: JSON.stringify(payload),
  });
  const book = remote || {
    id: Date.now(),
    ...payload,
    owner: payload.ownerName,
    initial: form.get("title").charAt(0).toUpperCase(),
    art: "art-coral",
    status: "Available",
  };
  if (!remote) {
    BookSwap.books.unshift(book);
    BookSwap.saveBooks(BookSwap.books);
  }
  const message = event.currentTarget.querySelector(".form-message");
  message.textContent = "Book added to your shelf.";
  message.style.color = "#59805b";
  event.currentTarget.reset();
}
