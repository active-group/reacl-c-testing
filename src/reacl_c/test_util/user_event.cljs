(ns reacl-c.test-util.user-event
  "Functions for simulating advanced DOM interactions, based
  on [@testing-library/user-event](https://testing-library.com/docs/ecosystem-user-event).

  Note: options to each function are \"clojurized\", i.e. camelcase
  and with a question mark for boolean options.
  "
  (:require ["@testing-library/user-event$default" :as userEvent])
  (:refer-clojure :exclude [type]))

;; Note: v0.14 of userEvent requires ES6; sticking with 0.13 for now

;; New in v0.14: pointer, copy, cut, (setup)

(defn- options-map [v f]
  (if (map? v)
    (f v)
    v))

(defn- to-js-opts [m]
  (fn [options]
    (when (some? options)
      (reduce-kv (fn [o k v]
                   (if (contains? options v)
                     (do (aset o k (get options v))
                         o)
                     o))
                 #js {}
                 m))))

(def ^:private pointer-js-opts
  (to-js-opts {"skipPointerEventsCheck" :skip-pointer-events-check?}))

(defn click
  "Clicks element. See [here](https://testing-library.com/docs/ecosystem-user-event#clickelement-eventinit-options)
  for details and options."
  ([element]
   (userEvent/click element))
  ([element event-init]
   (click element event-init {}))
  ([element event-init options]
   (userEvent/click element (clj->js event-init) (options-map options pointer-js-opts))))

(defn dbl-click
  "Clicks element. See [here](https://testing-library.com/docs/ecosystem-user-event#dblclickelement-eventinit-options)
  for details and options."
  ([element]
   (userEvent/dblClick element))
  ([element event-init]
   (dbl-click element event-init {}))
  ([element event-init options]
   (userEvent/dblClick element (clj->js event-init) (options-map options pointer-js-opts))))

(let [js-opts (to-js-opts {"delay" :delay ;; milliseconds ?!
                           "skipClick" :skip-click?
                           "skipAutoClose" :skip-auto-close?
                           "initialSelectionStart" :initial-selection-start
                           "initialSelectionEnd" :initial-selection-end
                           "autoModify" :auto-modify?
                           })]
  (defn type
    "Writes text inside an input or
  textarea. See [here](https://testing-library.com/docs/ecosystem-user-event#typeelement-text-options)
  for details and options."
    [element text & [options]]
    (userEvent/type element text (options-map options js-opts))))

(defn keyboard
  "Allows to simulate interactions with a keyboard.
  See [here](https://testing-library.com/docs/ecosystem-user-event#keyboardtext-options) for
  details."
  [actions]
  (userEvent/keyboard actions))

(let [js-opts (to-js-opts {"applyAccept" :apply-accept?})
      js-file (fn [file]
                (if (sequential? file)
                  (to-array file)
                  file))]
  (defn upload
    "Uploads file to an
  input. See [here](https://testing-library.com/docs/ecosystem-user-event#uploadelement-file--clickinit-changeinit--options)
  for details and options."
    ([element file]
     (userEvent/upload element (js-file file)))
    ([element file options]
     (userEvent/upload element (js-file file) (options-map options js-opts)))
    ([element file clickOrChangeInit options]
     (userEvent/upload element (js-file file) (clj->js clickOrChangeInit) (options-map options js-opts)))))

(defn clear
  "Selects the text inside an input or textarea and deletes it."
  [element]
  (userEvent/clear element))

(def ^:private select-js-opts pointer-js-opts)

(defn select-options
  "Selects the specified option(s) of a select element.
   See [here](https://testing-library.com/docs/ecosystem-user-event#selectoptionselement-values-options) for details and options."
  ([element values]
   (select-options element values nil))
  ([element values options]
   ;; options: string or option node.
   (userEvent/selectOptions element
                            (if (sequential? values)
                              (to-array values)
                              values)
                            (options-map options select-js-opts))))

(defn deselect-options
  "Remove the selection for the specified option(s) of a select element.
   See [here](https://testing-library.com/docs/ecosystem-user-event#deselectoptionselement-values-options) for details and options."
  ([element values]
   (deselect-options element values nil))
  ([element values options]
   (userEvent/deselectOptions element
                              (if (sequential? values)
                                (to-array values)
                                values)
                              (options-map options select-js-opts))))


(let [js-opts (fn [options] #js {:shift (:shift? options)})]
  (defn tab
    "Fires a tab
  event. See [here](https://testing-library.com/docs/ecosystem-user-event#tabshift-focustrap)
  for details and options."
    ([]
     (userEvent/tab))
    ([options]
     ;; different behaviour depending on :shift nil|true|false
     (if (some? (:shift? options))
       (userEvent/tab (:shift? options))
       (userEvent/tab)))))

(def ^:private hover-js-opts pointer-js-opts)

(defn hover
  "Hovers over element.
  See [here](https://testing-library.com/docs/ecosystem-user-event#hoverelement-options)
  for details and options."
  ([element]
   (hover element nil))
  ([element options]
   (userEvent/hover element (options-map options hover-js-opts))))

(defn unhover
  "Unhovers out of element.
  See [here](https://testing-library.com/docs/ecosystem-user-event#hoverelement-options)
  for details and options."
  ([element]
   (unhover element nil))
  ([element options]
   (userEvent/unhover element (options-map options hover-js-opts))))



(let [js-opts (to-js-opts {"initialSelectionStart" :initial-selection-start
                           "initialSelectionEnd" :initial-selection-end})]
  (defn paste
    "Paste data into the document.
     See [here](https://testing-library.com/docs/ecosystem-user-event#pasteelement-text-eventinit-options) for details and options."
    ([element text]
     (paste element text nil))
    ([element text options]
     ;; data: DataTransfer or String
     (userEvent/paste element text (options-map options js-opts)))
    ([element text eventInit options]
     (userEvent/paste element text (clj->js eventInit) (options-map options js-opts)))))
