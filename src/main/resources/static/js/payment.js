window.addEventListener("load", function () {
  const start = sessionStorage.getItem("checkin-dates");
  const end = sessionStorage.getItem("checkout-dates");
  const title = sessionStorage.getItem("room-info-title");
  const addr = sessionStorage.getItem("room-addr-info");
  const name = sessionStorage.getItem("host_name");
  const totalPrice = sessionStorage.getItem("room_info_price");

  if (start && end) {
    document.getElementById("checkin-dates").textContent = start;
    document.getElementById("checkout-dates").textContent = end;
  }
  document.getElementById("room-info-title").textContent = title;
  document.getElementById("room-addr-info").textContent = addr;
  document.getElementById("host_name").textContent = name;
  document.getElementById(
      "room_info_price").textContent = `총 가격: ${totalPrice.toLocaleString()} 원`;

})

// 결제창 함수 넣어주기
document.getElementById('payment-button').addEventListener("click",
    paymentProcess);

var IMP = window.IMP;
var isLogin = true; // 나중에 로그인 될때만 처리되고 로그인안될때는 못하게 막아줘야 ? 서버단에서??

function generateMerchantUid() {
  var today = new Date();
  var hours = today.getHours(); // 시
  var minutes = today.getMinutes();  // 분
  var seconds = today.getSeconds();  // 초
  var milliseconds = today.getMilliseconds(); // 밀리초

  // 시, 분, 초, 밀리초를 결합하여 고유한 문자열 생성
  return `${hours}${minutes}${seconds}${milliseconds}`;
}

// rsp는 response
function paymentProcess() {
  const userEmail = document.querySelector('input[name="userEmail"]').value; // user 테이블 email
  const username = document.querySelector('input[name="userName"]').value; // user 테이블 name
  const title = sessionStorage.getItem("room-info-title"); // 함수 내에서 title을 가져옴
  const totalPrice = Number(sessionStorage.getItem("room_info_price"));
  const userId = Number(document.querySelector('input[name="userId"]').value); // 숫자로 변환
  // const accommodationId = Number(sessionStorage.getItem("room-info-title"));
  const checkInDate = sessionStorage.getItem("checkin-dates");
  const checkOutDate = sessionStorage.getItem("checkout-dates");

  console.log("Total Price:", totalPrice);

  const urlPath = window.location.pathname;
  const parts = urlPath.split('/');
  const accommodationId = Number(parts[parts.length - 1]);

  if (confirm("구매 하시겠습니까?")) {
    if (isLogin) { // 회원만 결제 가능
      IMP.init("imp15548812"); // 고객사 식별코드 (포트원 사이트)
      IMP.request_pay({
        pg: 'kakaopay.TC0ONETIME', // PG사 코드표에서 선택 (포트원 사이트)
        pay_method: 'point', // 결제 방식
        merchant_uid: "IMP" + generateMerchantUid(), // 결제 고유 번호
        name: title, // 방이름??
        amount: totalPrice, // reservation의 total_price

        /* 구매자 정보 */
        buyer_email: `${userEmail}`,
        buyer_name: `${username}`,
        // buyer_tel : '010-1234-5678',
        // buyer_addr : '서울특별시 강남구 삼성동',
        // buyer_postcode : '123-456'
      }, async function (rsp) { // callback
        if (rsp.success) { //결제 성공시

          // 필요한 데이터를 추가 (우리가 넣을 데이터)
          // rsp.partnerId = 12345;  // partnerId 값 설정 (필요시 동적으로 가져올 수 있음) 숙소 호스트 ID?
          rsp.userId = userId;     // userId 값 설정 (현재 로그인한 사용자 정보로 설정 가능)
          rsp.reservationId = null;   // reservationId 값 설정 (주문 관련 정보로 설정 가능) 예약 id로 가는 걸로
          rsp.paymentDate = new Date().toISOString().split('T')[0];  // paymentDate를 현재 날짜로 설정 (yyyy-mm-dd 형식)

          console.log(rsp);
          // Send the payment details to your Spring Boot backend
          // fetch 요청을 보내고 await를 사용하면, 서버의 응답이 올 때까지 코드 실행이 잠시 멈추고,
          // 그 응답값을 기다린 후에 다음 코드를 실행합니다.
          const response = await fetch('/api/payment/portone', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            // body: JSON.stringify(rsp) // Send the response object
            body: JSON.stringify({
              paymentRequest: rsp,
              reservationRequest: {
                userId: userId,
                accommodationId: accommodationId,
                checkInDate: checkInDate,
                checkOutDate: checkOutDate,
                totalPrice: totalPrice,
                status: 1
              }
            })
          });

          const result = await response.text(); // String으로 바꾸려면 response.text()t로?
          // const result = await response.json(); // String으로 바꾸려면 response.text()로?
          console.log(result)

          if (rsp.status == "paid") { // DB저장 성공시
            alert('결제 완료!');
            window.location.href = '/guest/booking/list';
          } else { // 결제완료 후 DB저장 실패시
            alert(`error:[${rsp.status}]\n결제요청이 승인된 경우 관리자에게 문의바랍니다.`);
            // DB저장 실패시 status에 따라 추가적인 작업 가능성
          }
        } else if (rsp.success == false) { // 결제 실패시
          alert(rsp.error_msg)
        }
      });
    } else { // 비회원 결제 불가
      alert('로그인이 필요합니다!')
    }
  } else { // 구매 확인 알림창 취소 클릭시 돌아가기
    return false;
  }
}