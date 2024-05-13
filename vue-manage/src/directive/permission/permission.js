import store from '../../store/index'

// 权限判断函数
function checkPermission(el, binding) {
    const { value } = binding
    const roles = store.getters['getAuthority']

    if (value) {
        const permissionRoles = value // 获取到权限数组
        const hasPermission = roles.some(role => {
            return permissionRoles.includes(role) // 判断是否有权限
        })

        if (!hasPermission) { // 如果没有权限，就删除元素
            el.style.display = 'none'
        } else {
            el.style.display = ''
        }
    } else {
        throw new Error(`need roles! Like v-permission="['admin','editor']"`)
    }
}

// 初始化函数
function initFn(el, binding) {
    checkPermission(el, binding) // 如果获取到权限，就进行权限判断
   
    // 自定义属性
    const customProp = binding.value;

    // 监听store中的属性变化
    binding.instance.$watch(
      () => store.getters['getAuthority'],
      (storeProp) => {
        checkPermission(el, binding)  // 在store中的属性变更后执行回调函数
        // console.info(` %c${customProp} changed to ${storeProp}`, "font-style: italic; background-color: #409EFF;padding: 2px");
      }
    );
}

export default {
    mounted: (el, binding) => initFn(el, binding),
    updated: (el, binding) => checkPermission(el, binding)
}
