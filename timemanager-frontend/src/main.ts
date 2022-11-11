import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from "./store/index";
import PrimeVue from 'primevue/config'
import ButtonPrime from 'primevue/button'
import InputText from 'primevue/inputtext'
import ToastService from 'primevue/toastservice'
import Toast from 'primevue/toast';
import DataTable from 'primevue/datatable';
import Toolbar from 'primevue/toolbar';
import Column from 'primevue/column';
import ColumnGroup from 'primevue/columngroup';     //optional for column grouping
import Row from 'primevue/row';                     //optional for row
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import Textarea from 'primevue/textarea';
import Paginator from 'primevue/paginator';
import Tooltip from 'primevue/tooltip';
import Dropdown from 'primevue/dropdown';
import Card from 'primevue/card';
import MultiSelect from 'primevue/multiselect';
import Tag from 'primevue/tag';
import Calendar from 'primevue/calendar';
import InputMask from 'primevue/inputmask';

import 'primevue/resources/themes/bootstrap4-dark-blue/theme.css'
import 'primevue/resources/primevue.min.css'
import 'primeicons/primeicons.css'

const app = createApp(App)
app.use(router)
app.use(store)
app.use(PrimeVue)
app.use(ToastService)

app.component('InputText', InputText)
app.component('ButtonPrime', ButtonPrime)
app.component('DataTable', DataTable)
app.component('Column', Column)
app.component('ColumnGroup', ColumnGroup)
app.component('Row', Row)
app.component('Toolbar', Toolbar)
app.component('Button', Button)
app.component('Dialog', Dialog)
app.component('Textarea', Textarea)
app.component('Toast', Toast)
app.component('Paginator', Paginator)
app.component('Dropdown', Dropdown)
app.component('Card', Card)
app.component('MultiSelect', MultiSelect)
app.component('Tag', Tag)
app.component('Calendar', Calendar)
app.component('InputMask', InputMask)

app.directive('tooltip', Tooltip);
app.mount('#app')
