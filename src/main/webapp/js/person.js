
    var
        fileInput = document.getElementById('test-image-file'),
        info = document.getElementById('test-file-info'),
        preview = document.getElementById('test-image-preview');
        
// 监听change事件:
fileInput.addEventListener('change', function() {
			// 清除背景图片:
			preview.style.backgroundImage = '';
			// 检查文件是否选择:
			if (!fileInput.value) {
				info.innerHTML = '没有选择文件';
				return;
			}
			// 获取File引用:
			var file = fileInput.files[0];
			//判断文件大小
			var size = file.size;
			if (size >= 1 * 1024 * 1024) {
				alert('文件大小超出限制');
				return false;
			}
			// 获取File信息:
			info.innerHTML = '文件名称: ' + file.name + '<br>' +
				'文件大小: ' + file.size + '<br>' +
				'上传时间: ' + file.lastModifiedDate;
			if (file.type !== 'image/jpeg' && file.type !== 'image/png' && file.type !== 'image/gif') {
					alert('不是有效的图片文件!');
					return;
				}
				// 读取文件:
				var reader = new FileReader();
				reader.onload = function(e) {
					var
						data = e.target.result;         
					preview.style.backgroundImage = 'url(' + data + ')';
				};
				// 以DataURL的形式读取文件:
				reader.readAsDataURL(file);
				
			});

			
function welcome(){
	window.alert("此页面功能尚未开放");
}