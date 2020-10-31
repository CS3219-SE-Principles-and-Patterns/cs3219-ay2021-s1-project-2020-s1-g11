import {createLocalVue, shallowMount} from '@vue/test-utils'
import ElementUI from 'element-ui';
import VueRouter from 'vue-router';
import Vuex from 'vuex';
import EmptySection from "./../components/emptyStates/EmptySection.vue";

const localVue = createLocalVue();

localVue.use(Vuex);
localVue.use(VueRouter);
localVue.use(ElementUI);

describe("EmptySection.vue", () => {
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
    const wrapper = shallowMount(EmptySection, {store, localVue})
    expect(wrapper.element).toMatchSnapshot()
  });
});