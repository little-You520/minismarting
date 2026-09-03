<template>
  <div id="app">
    <h1 class="title">📹 工地视频监控系统</h1>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="camera-grid">
      <div v-for="camera in cameras" :key="camera.id" class="camera-card">
        <div class="camera-header">
          <span class="camera-id">摄像头 #{{ camera.id }}</span>
          <span class="status" :class="camera.status === '0' ? 'online' : 'offline'">
            {{ camera.status === '0' ? '在线' : '离线' }}
          </span>
        </div>
        <div class="video-container">
          <!-- 使用 wvpOpenUrl（映射后的统一字段） -->
          <video
            v-if="camera.wvpOpenUrl"
            class="hls-video"
            :data-stream="camera.wvpOpenUrl"
            controls
            autoplay
            muted
            style="width:100%;height:100%;"
          ></video>
          <div v-else class="no-video">暂无推流地址</div>
        </div>
        <div class="camera-info">
          <p>设备ID：{{ camera.assetId || '未关联' }}</p>
          <p>AI监控类型：{{ camera.aiMonitorType || '未配置' }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import axios from 'axios'
import Hls from 'hls.js'

const cameras = ref([])
const loading = ref(true)
const error = ref(null)

// 生成播放地址
const getFullStreamUrl = (streamName) => {
  if (!streamName) return ''
  let name = streamName
  if (streamName.includes('rtmp://')) {
    name = streamName.split('/').pop()
  }
  const baseUrl = import.meta.env.VITE_HLS_BASE_URL || 'http://localhost:8068'
  return `${baseUrl}/hls/${name}/index.m3u8`
}

// 初始化所有 video
const initAllVideos = () => {
  const videos = document.querySelectorAll('.hls-video')
  console.log('找到 video 元素数量:', videos.length)

  videos.forEach((video, index) => {
    const streamName = video.dataset.stream
    if (!streamName) {
      console.warn(`第 ${index} 个 video 缺少 data-stream`)
      return
    }
    const url = getFullStreamUrl(streamName)
    console.log(`第 ${index} 个 video 播放地址:`, url)

    if (Hls.isSupported()) {
      const hls = new Hls({
        enableWorker: true,
        lowLatencyMode: true,
      })
      hls.loadSource(url)
      hls.attachMedia(video)
      hls.on(Hls.Events.MANIFEST_PARSED, () => {
        video.play().catch(e => console.warn('自动播放被阻止:', e))
      })
      hls.on(Hls.Events.ERROR, (e, data) => {
        console.error(`第 ${index} 个 video HLS错误:`, data)
      })
    } else if (video.canPlayType('application/vnd.apple.mpegurl')) {
      video.src = url
      video.addEventListener('loadedmetadata', () => {
        video.play().catch(e => console.warn('自动播放被阻止:', e))
      })
    } else {
      console.warn('浏览器不支持 HLS')
    }
  })
}

const fetchCameras = async () => {
  try {
    loading.value = true
    const response = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/cameras`)
    console.log('=== 原始数据（第一个对象）===')
    console.log(response.data[0])  // 打印第一个对象的所有属性
    console.log('=== 所有键名 ===')
    console.log(Object.keys(response.data[0])) // 打印键名列表

    // 直接赋值原始数据，不做任何处理
    cameras.value = response.data
    loading.value = false
    await nextTick()
    setTimeout(() => {
      initAllVideos()
    }, 300)
  } catch (err) {
    error.value = '加载摄像头列表失败：' + err.message
    loading.value = false
  }
}

onMounted(() => {
  fetchCameras()
})
</script>

<style scoped>
/* 样式保持不变 */
.title { padding: 20px; color: #2c3e50; border-bottom: 2px solid #3498db; }
.loading, .error { text-align: center; padding: 50px; font-size: 18px; }
.error { color: red; }
.camera-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
  padding: 20px;
}
.camera-card {
  border: 1px solid #ddd;
  border-radius: 10px;
  overflow: hidden;
  background: #f9f9f9;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.camera-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #fff;
  border-bottom: 1px solid #eee;
}
.camera-id { font-weight: bold; font-size: 16px; }
.status { padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: bold; }
.online { background: #27ae60; color: white; }
.offline { background: #e74c3c; color: white; }
.video-container {
  background: #000;
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.video-container video { width: 100%; height: 100%; }
.no-video { color: #888; font-size: 14px; }
.camera-info { padding: 12px 16px; font-size: 14px; color: #555; }
.camera-info p { margin: 4px 0; }
</style>