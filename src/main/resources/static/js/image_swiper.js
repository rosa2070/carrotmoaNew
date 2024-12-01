let currentImageIndex = 0;
const images = document.querySelectorAll('.room-image-wrapper');
const totalImages = images.length;
const imageIndexDisplay = document.getElementById('imageIndex');

function showImage(index) {
  images.forEach((imgWrapper, i) => {
    imgWrapper.style.display = i === index ? 'block' : 'none';
    imgWrapper.style.opacity = i === index ? '1' : '0';
  });
  imageIndexDisplay.textContent = `${index + 1}/${totalImages}`;
}

function nextImage() {
  currentImageIndex = (currentImageIndex + 1) % totalImages;
  showImage(currentImageIndex);
}

function prevImage() {
  currentImageIndex = (currentImageIndex - 1 + totalImages) % totalImages;
  showImage(currentImageIndex);
}

// 초기 이미지 보여주기
document.addEventListener('DOMContentLoaded', () => {
  showImage(currentImageIndex);
});