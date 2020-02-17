var app = new Vue({
    el: '#add',
    data: {
        dict:{},
    },
    methods: {
        addServer(isAction=true){

            var data = "";

            var sdata = document.getElementById('data').value;

            axios.get("http://localhost:8080/getbyname?name="+sdata).then(response =>{
                var incoming = response.data;
                var len = incoming.length;
                var dict = {};

                for(i=0; i<len;i++){
                        dict[i] = incoming[i];
                }

                this.dict = dict;

            }).catch(error => {
                      if (error.response && isAction) {
                        var dict = {};
                        this.dict = dict;
                        alert("No such record found");
                      }
                      else if (error.response && !isAction) {
                        var dict = {};
                        this.dict = dict;
                       }
                       else{
                        console.log(error.response);
                       }

                    });

        },
        deleteServer(id){
            axios.delete("http://localhost:8080/delete?id="+id);
            this.addServer(false);
        }
    }
});