const Password = /(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-])[a-zA-Z0-9#?!@$%^&*-]{8,32}$/
let $newPassword = $('#new-password');
let $newPasswordCheck = $('#new-password-check');
$newPassword.on('input', function () {
  let password = $(this).val();
  let length = password.length;
  if (Password.test(password)) {
    if (length >= 8 && length <= 16) {
      $newPasswordCheck.text('사용 가능하지만 안전하지 않을거같아요');
      $newPassword.css("border", "2px solid orange");
    } else if (length >= 17 && length <= 26) {
      $newPasswordCheck.text('적당한 비밀번호에요');
      $newPassword.css("border", "2px solid #8ec96d");
    } else if (length >= 27 && length <= 36) {
      $newPasswordCheck.text('안전한 비밀번호에요');
      $newPassword.css("border", "2px solid blue");
    }
  } else if (length > 36) {
    $newPasswordCheck.text('비밀번호가 너무 길어요');
    $newPassword.css("border", "2px solid red");
  } else {
    $newPasswordCheck.text('비밀번호가 안전하지 않아요');
    $newPassword.css("border", "2px solid red");
  }
});