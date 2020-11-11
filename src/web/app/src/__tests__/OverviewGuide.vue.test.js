import {createLocalVue, shallowMount} from '@vue/test-utils'
import ElementUI from 'element-ui';
import VueRouter from 'vue-router';
import Vuex from 'vuex';
import OverviewGuide from "./../components/userPageDetail/OverviewGuide.vue";

const localVue = createLocalVue();

localVue.use(Vuex);
localVue.use(VueRouter);
localVue.use(ElementUI);

describe("OverviewGuide.vue", () => {
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
    const wrapper = shallowMount(OverviewGuide, {store, localVue})
    expect(wrapper.element).toMatchSnapshot()
  });
});