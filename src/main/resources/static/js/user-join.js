const regPassword = /(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-])[a-zA-Z0-9#?!@$%^&*-]{8,32}$/
const regEmail = /^([a-z0-9_.-]+)@([\da-z.-]+)\.([a-z.]{2,6})$/
const regNickname = /^([a-zA-Z가-힣0-9]){2,12}$/
const regAccount = /([0-9\-]{3,6}-[0-9\-]{2,6}-[0-9\-]{1,6})/

let $email = $('#email');
let $check = $('#check');
let $authentication = $('#authentication');
let $authCodeCertified;
let $authenticationMail;
let $duplicationCheck = $('#duplication-check');
let $password = $('#password');
let $passwordCheck = $('#password-check');
let $passwordReconfirm = $('#password-reconfirm');
let $passwordReconfirmCheck = $('#password-reconfirm-check');
let $nickname = $('#nickname');
let $joinButton = $('#join');
let $account = $('#account');
let $bankName = $('#bank-name');
let $account_holder = $('#account-holder');
let $nicknameDuplication = $('#nickname-duplication')
$('#timer').hide();
let email;
let timerId;

let passwordFlag = false;
let authCodeFlag = false;
let nicknameFlag = false;

function joinSubmit() {
  if (authCodeFlag && passwordFlag && nicknameFlag) {
    $joinButton.prop('disabled', false);
    $joinButton.css('background-color', 'rgb(255, 111, 15)');
    $joinButton.css('cursor', 'pointer');
  } else {
    $joinButton.prop('disabled', true);
    $joinButton.css('background-color', 'rgb(255, 185, 139)');
    $joinButton.css('cursor', 'cursor');

  }
}

$email.on('input', function () {
  email = $(this).val();
  if (regEmail.test(email)) {
    $duplicationCheck.prop('disabled', false);
    $check.text('');
  } else {
    $check.text('유효하지 않은 이메일이에요');
    $duplicationCheck.prop('disabled', true);
  }
})

$password.on('input', function () {
  let password = $(this).val();
  let length = password.length;
  if (regPassword.test(password)) {
    if (length >= 8 && length <= 16) {
      $passwordCheck.text('사용 가능하지만 안전하지 않을거같아요');
      $password.css("border", "2px solid orange");
    } else if (length >= 17 && length <= 26) {
      $passwordCheck.text('적당한 비밀번호에요');
      $password.css("border", "2px solid #8ec96d");
    } else if (length >= 27 && length <= 36) {
      $passwordCheck.text('안전한 비밀번호에요');
      $password.css("border", "2px solid blue");
    }
  } else if (length > 36) {
    $passwordCheck.text('비밀번호가 너무 길어요');
    $password.css("border", "2px solid red");
  } else {
    $passwordCheck.text('비밀번호가 안전하지 않아요');
    $password.css("border", "2px solid red");
  }
});

$passwordReconfirm.on('input', function () {
  if (passwordCheck()) {
    $passwordReconfirmCheck.text('비밀번호가 일치해요');
    passwordFlag = true;
    joinSubmit();
  } else {
    passwordFlag = false;
    $passwordReconfirmCheck.text('비밀번호가 일치하지 않아요');
  }
});

function passwordCheck() {
  return ($password.val() !== '') && ($passwordReconfirm.val()
      === $password.val() && regPassword.test($password.val()));
}

$duplicationCheck.click(function () {
  email = $email.val();
  console.log(email);
  $.ajax({
    url: '/api/user/email-check',
    data: {email: email},
    type: 'get',
    success: function (result) {
      if (result) {
        $check.text('사용중인 이메일입니다.');
      } else {
        $check.text('사용 가능한 이메일입니다.');
        $authentication.html(
            "<input type='button' id='use-email' value='이 이메일 사용할래요'/>");
      }
    }
  })
});

$authentication.on('click', '#use-email', function () {
  $authentication.html("<input type='text' id='auth-code'/> " +
      "<input type='button' id='authentication-mail' value='인증코드 발송' style='height:40px;margin-left:20px;'/>");
  $authenticationMail = $('#authentication input#authentication-mail');
  $email.prop('readonly', true);
  $duplicationCheck.hide();
  $check.text('');
  $authentication.append(
      "<input type='button' id='auth-code-certified' value='인증' style='height:40px;margin-left:20px;'/>");
  $authCodeCertified = $('#authentication input#auth-code-certified');
  $authCodeCertified.hide();
});

$authentication.on('click', '#authentication-mail', function () {
  $authCodeCertified.val('인증');
  $authCodeCertified.prop('disabled', false);

  $.ajax({
    url: '/api/user/auth-code-send',
    data: {email: email},
    type: 'get',
    success: function (data) {
      if (data) {
        $authenticationMail.val('재발송');
        $authCodeCertified.show();
        let timeLimit = 180;
        $('#timer').show();
        timerInterval(timeLimit);

      } else {
        alert('메일발송 오류');
      }
    }
  })
});

$authentication.on('click', '#auth-code-certified', function () {
  let inputAuthCode = $('#auth-code').val();
  console.log(email);
  console.log(inputAuthCode);

  $.ajax({
    url: '/api/user/auth-code-certified',
    data: {email: email, inputauthcode: inputAuthCode},
    type: 'get',
    success: function (data) {
      if (data) {
        console.log('ok');
        $('#timer').hide();
        $authCodeCertified.hide();
        $authenticationMail.prop('disabled', true);
        $authenticationMail.val('인증완료');
        if (timerId) {
          clearInterval(timerId);
        }
        authCodeFlag = true;
        joinSubmit();
      } else {
        $check.text("인증코드와 맞지않아요");
        authCodeFlag = false;
        joinSubmit();
      }
    }
  })
});

function timerInterval(timeLimit) {
  if (timerId) {
    clearInterval(timerId);
  }

  timerId = setInterval(function () {
    let minutes = Math.floor(timeLimit / 60);
    let second = timeLimit % 60;
    $('#minutes').text(minutes.toString().padStart(2, '0'));
    $('#seconds').text(second.toString().padStart(2, '0'));
    timeLimit--;
    if (timeLimit < 0) {
      clearInterval(timerId);
      $authCodeCertified.prop('disabled', true);
      $authCodeCertified.val('만료');
    }
  }, 1000);
}

$nickname.on('input', function () {
  if (regNickname.test(($nickname).val()) && $nickname.val() !== '') {

    nicknameDuplicationCheck($nickname.val()).then(check => {
      if (check) {
        console.log('사용 가능')
        $('#nickname-duplication').text('사용 가능한 닉네임이에요');
        nicknameFlag = true;
        joinSubmit();
      } else {
        console.log('이미 사용중')
        $('#nickname-duplication').text('이미 사용중인 닉네임이에요');
      }
    });

  } else {
    $('#nickname-duplication').text('닉네임은 최소 두 자 이상을 사용해야해요');
    nicknameFlag = false;
    joinSubmit();
  }
});
$('#authority-type').on('change', function (event) {
  if (event.target.checked) {
    $('#host').css("display", 'flex');
  } else {
    $('#host').css("display", 'none');
  }
});
if (document.getElementById('authority-type')) {

  document.getElementById('authority-type').addEventListener('change',
      function (event) {
        if (event.target.checked) {
          $('#host').css("display", 'flex');
        } else {
          $('#host').css("display", 'none');
        }
      });
}
if (document.getElementById('join-form')) {

  document.getElementById('join-form').addEventListener('submit',
      function (event) {
        event.preventDefault();
        const formData = new FormData(this);
        const data = {};
        formData.forEach((value, key) => {
          data[key] = value;
        })
        fetch("/api/user/join", {
          method: "POST",
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
        })
        .then(response => {
          if (!response.ok) {
            console.log("값 안옴")
          }
          return response.json()
        })

        .then(result => {
          if (result) {
            alert('가입되셨습니다.');
            window.location.href = "/";
          } else {
            console.log("가입 실패");
          }
        });

      })
}

function nicknameDuplicationCheck(nickname) {
  console.log(nickname)
  return fetch(`/api/user/nickname-duplication?nickname=${nickname}`, {
    method: "get",
  })
  .then(response => {
    if (!response.ok) {
      console.log("값 안옴")
    }
    return response.json();
  })

  .then(result => {
    console.log(result);
    return !result;
  })
  .catch(error => {
    console.log('error', error);
    return false;
  })
}