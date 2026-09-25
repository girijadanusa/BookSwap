document.addEventListener("DOMContentLoaded", () => {
  const selected = JSON.parse(
    localStorage.getItem("bookswap-selected-book") || "null",
  );
  if (selected)
    document.querySelector("#selected-title").textContent = selected.title;
  document
    .querySelector("#exchange-form")
    ?.addEventListener("submit", async (event) => {
      event.preventDefault();
      const form = new FormData(event.currentTarget);
      const user = JSON.parse(localStorage.getItem("bookswap-user") || "{}");
      const request = {
        ...Object.fromEntries(form),
        bookId: selected?.id || 1,
        requesterId: user.id || "guest",
        requesterName: user.name || form.get("name"),
      };
      const remote = await BookSwap.request("/api/exchanges", {
        method: "POST",
        body: JSON.stringify(request),
      });
      const message = event.currentTarget.querySelector(".form-message");
      message.textContent = remote
        ? "Request sent to the book owner."
        : "Request saved locally. Start the Java server to sync it.";
      message.style.color = "#59805b";
      event.currentTarget.reset();
    });
});
