<template>

<iframe :src="adminSrc" width="100%" height="100%"></iframe>

</template>


<script>
import baseURL_ from '@/utils/baseUrl.js';
 export default {
        data() {
            return {
               adminSrc:baseURL_.adminUrl
            }
        },
 }


</script>