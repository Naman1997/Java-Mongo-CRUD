var app = new Vue({
    el: '#app',
    data: {
        message: 'Projects',
        dict:{}
    },
    created: function(){
        this.getResult();
    },
    methods: {
        getResult(){
            axios.get('http://localhost:8080/getall').then(response =>{
                var incoming = response.data;
                var len = incoming.length;
                var dict = {};

                for(i=0; i<incoming.length;i++){
                        dict[i] = incoming[i];
                }

                this.dict = dict;

            });
        },
        addServer(){

            var id = "";
            var name = "";
            var language = "";
            var framework = "";

            var sid = document.getElementById('AId').value;
            var sname = document.getElementById('AName').value;
            var slanguage = document.getElementById('ALanguage').value;
            var sframework = document.getElementById('AFramework').value;


            const url = "http://localhost:8080/servers";
            axios.put(url, {
                name: sname,
                id: sid,
                language: slanguage,
                framework: sframework
              });

        },
        deleteServer(id){
            axios.delete("http://localhost:8080/delete?id="+id);
            window.location.reload();
        }
    }
});