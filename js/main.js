const BOOKSWAP_API = "http://localhost:8080";
const sampleBooks = [
  {
    id: 1,
    title: "Atomic Habits",
    author: "James Clear",
    owner: "Maya S.",
    category: "Skills",
    type: "Exchange",
    condition: "Good",
    city: "Bangalore",
    art: "art-coral",
    initial: "A",
  },
  {
    id: 2,
    title: "The Psychology of Money",
    author: "Morgan Housel",
    owner: "Riya M.",
    category: "Skills",
    type: "Borrow",
    condition: "New",
    city: "Bangalore",
    art: "art-gold",
    initial: "P",
  },
  {
    id: 3,
    title: "The Design of Everyday Things",
    author: "Don Norman",
    owner: "Sam K.",
    category: "Academic",
    type: "Give Away",
    condition: "Used",
    city: "Bangalore",
    art: "art-ink",
    initial: "D",
  },
  {
    id: 4,
    title: "Norwegian Wood",
    author: "Haruki Murakami",
    owner: "Neha P.",
    category: "Fiction",
    type: "Exchange",
    condition: "Good",
    city: "Bangalore",
    art: "art-green",
    initial: "N",
  },
  {
    id: 5,
    title: "Clean Code",
    author: "Robert C. Martin",
    owner: "Arjun R.",
    category: "Academic",
    type: "Borrow",
    condition: "Good",
    city: "Bangalore",
    art: "art-ink",
    initial: "C",
  },
  {
    id: 6,
    title: "The Alchemist",
    author: "Paulo Coelho",
    owner: "Isha V.",
    category: "Fiction",
    type: "Give Away",
    condition: "Used",
    city: "Bangalore",
    art: "art-gold",
    initial: "T",
  },
];
window.BookSwap = {
  api: BOOKSWAP_API,
  books:
    JSON.parse(localStorage.getItem("bookswap-books") || "null") || sampleBooks,
  saveBooks(books) {
    localStorage.setItem("bookswap-books", JSON.stringify(books));
  },
  async request(path, options = {}) {
    try {
      const response = await fetch(`${BOOKSWAP_API}${path}`, {
        headers: { "Content-Type": "application/json" },
        ...options,
      });
      if (!response.ok) throw new Error("API unavailable");
      return await response.json();
    } catch (error) {
      return null;
    }
  },
};
function bookCard(book) {
  return `<article class="book-card"><a href="book-details.html?id=${book.id}"><div class="book-art ${book.art || "art-coral"}"><small>${book.category || "BOOK"}</small><strong>${book.initial || book.title.charAt(0)}</strong></div><div class="card-content"><h3>${book.title}</h3><p>${book.author} · ${book.owner}</p><div class="card-footer"><span class="type-tag">${book.type}</span><span class="availability">Available</span></div></div></a></article>`;
}
document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll('a[href^="#"]').forEach((link) =>
    link.addEventListener("click", (event) => {
      if (link.getAttribute("href") === "#") event.preventDefault();
    }),
  );
});
