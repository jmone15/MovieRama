import {toast} from "react-toastify";

export function parseJwt(token) {
    if (!token) {
        return
    }
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace('-', '+').replace('_', '/')
    return JSON.parse(window.atob(base64))
}

export const handleLogError = (error) => {
    if (error.response) {
        console.log(error.response.data)
        toast.error("Error: " + error.response.data.errorMessage)
    } else if (error.request) {
        console.log(error.request)
        toast.error("Error: " + error.request)
    } else {
        console.log(error.message)
        toast.error("Error: " + error.message)
    }
}