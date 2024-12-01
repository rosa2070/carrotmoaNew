// window.addEventListener("load", function () {
//     const start = sessionStorage.getItem("checkin-dates");
//     const end = sessionStorage.getItem("checkout-dates");
//     const title = sessionStorage.getItem("room-info-title");
//     const addr = sessionStorage.getItem("room-addr-info");
//     const name = sessionStorage.getItem("host_name");
//     const totalPrice = sessionStorage.getItem("room_info_price");
//
//     if(start && end){
//         document.getElementById("checkin-dates").textContent = start;
//         document.getElementById("checkout-dates").textContent = end;
//     }
//     document.getElementById("room-info-title").textContent = title;
//     document.getElementById("room-addr-info").textContent = addr;
//     document.getElementById("host_name").textContent = name;
//     document.getElementById("room_info_price").textContent = `총 가격: ${totalPrice.toLocaleString()} 원`;
//
// })

// redis로 옮길 예정