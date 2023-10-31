/**
 *  utility.js
 * **/


export default class Utility {
    ajax(url, data, type) {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: url,
                data: JSON.stringify(data),
                type: type,
                dataType: "json",
                success: resolve,
                error: reject,
                contentType: "application/json"
            });
        });
    }
}

utility.ajax(url,data,type)
    .then((responseData) => {
        console.log(responseData)
    })
    .catch((error) => {
        console.error(error);
    });