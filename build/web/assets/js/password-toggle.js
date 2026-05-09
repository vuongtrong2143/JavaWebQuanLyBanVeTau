document.addEventListener("DOMContentLoaded", function () {
    var buttons = document.querySelectorAll(".password-toggle");

    buttons.forEach(function (button) {
        button.addEventListener("click", function () {
            var targetId = button.getAttribute("data-target");
            var input = document.getElementById(targetId);

            if (!input) {
                return;
            }

            var isHidden = input.getAttribute("type") === "password";

            input.setAttribute("type", isHidden ? "text" : "password");
            button.textContent = isHidden ? "🙈" : "👁";
            button.setAttribute("aria-label", isHidden ? "Ẩn mật khẩu" : "Hiện mật khẩu");
        });
    });
});
