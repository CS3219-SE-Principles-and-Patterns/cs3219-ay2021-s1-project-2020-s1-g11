import {createLocalVue, shallowMount} from '@vue/test-utils'
import ElementUI from 'element-ui';
import VueRouter from 'vue-router';
import Vuex from 'vuex';
import EmptyData from "./../components/emptyStates/EmptyData.vue";

const localVue = createLocalVue();

localVue.use(Vuex);
localVue.use(VueRouter);
localVue.use(ElementUI);

describe("EmptyData.vue", () => {
  let store;

  beforeEach(() => {
    store = new Vuex.Store({
      state: {
        isPageLoading: false,
        userInfo: {
          isApiError: false,
        },
        dbMetaData: {
          entitiesStatus: {
            isLoading: false,
          }
        }
      }
    })
  });

  it('renders correctly', () => {
    const wrapper = shallowMount(EmptyData, {store, localVue})
    expect(wrapper.element).toMatchSnapshot()
  });
});