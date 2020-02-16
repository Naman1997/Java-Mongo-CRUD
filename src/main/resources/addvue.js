var app = new Vue({ 
    el: '#add',
    data: {
        dict:{}
    },
    methods: {
        addServer(){

            var data = "";

            var sdata = document.getElementById('data').value;

            axios.get("http://localhost:8080/getbyname?name="+sdata).then(response =>{
                var incoming = response.data;
                var len = incoming.length;
                var dict = {};
                
                for(i=0; i<incoming.length;i++){
                        dict[i] = incoming[i];
                }

                this.dict = dict;

            });

        },
        deleteServer(id){
            axios.delete("http://localhost:8080/delete?id="+id);
            window.location.reload();
        }
    }
});