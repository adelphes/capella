// @ts-ignore
const android = Android;
  
const target = {
    message1: "hello",
    message2: "everyone"
  };

const handler3 = {
    get(target, prop, receiver) {
      android.proxyGet(target, prop, receiver);
      return Reflect.get(target, prop, receiver);
    },
  };
  
const proxy1 = new Proxy(target, handler3);

function showAndroidToast(toast) {
    // test unhandld exception failure
    return __dirname.length;
}

window.onerror = function myErrorHandler(errorMsg, url, lineNumber) {
    android.showToast(`Unhandled error [${lineNumber}]: ${errorMsg}`);
    return false;
}
