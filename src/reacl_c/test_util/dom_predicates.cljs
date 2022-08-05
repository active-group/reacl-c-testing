(ns reacl-c.test-util.dom-predicates
  "Utilities to test DOM nodes for certain properties, based
  on [jest-dom](https://github.com/testing-library/jest-dom)."
  
  (:require ["@testing-library/jest-dom/matchers" :as matchers]
            [clojure.string :as str]))

(defn- pass? [jest-result]
  ;; Note: we drop a possible helpful message here; would require 'assert-expr' extension, and some more libs though.
  #_(println ((.-message jest-result)))
  (aget jest-result "pass"))

(defn- call-p [f element & args]
  (pass? (apply f element args)))

(defn disabled?
  "Is the element disabled from the user's perspective?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tobedisabled)
  for details."
  [element]
  (call-p matchers/toBeDisabled element))

(defn enabled?
  "Is the element enabled from the user's perspective?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tobeenabled)
  for details."
  [element]
  (call-p matchers/toBeEnabled element))

(defn empty-dom-element?
  "Does the element have no visible content?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tobeemptydomelement)
  for details."
  [element]
  (call-p matchers/toBeEmptyDOMElement element))

(defn in-the-document?
  "Is this element present in the document?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tobeinthedocument)
  for details."
  [element]
  (call-p matchers/toBeInTheDocument element))

(defn invalid?
  "Is this element currently invalid?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tobeinvalid)
  for details."
  [element]
  (call-p matchers/toBeInvalid element))

(defn required?
  "Is this element currently required?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#toberequired)
  for details."
  [element]
  (call-p matchers/toBeRequired element))

(defn valid?
  "Is this element currently valid?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tobevalid)
  for details."
  [element]
  (call-p matchers/toBeValid element))

(defn visible?
  "Is this element currently visible to the user?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tobevisible)
  for details."
  [element]
  (call-p matchers/toBeVisible element))

(defn contained-element=
  "Does this element contain another element as a descendant?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tocontainelement)
  for details."
  [element sub-element]
  (call-p matchers/toContainElement element sub-element))

(defn contained-html=
  "Does this element contain a HTML element represented by the given
  string?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tocontainhtml)
  for details."
  [element html]
  (call-p matchers/toContainHTML element html))

(defn accessible-description=
  "Does this element have the specified accessible description, or any
  accessible description at all?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tohaveaccessibledescription)
  for details."
  ([element]
   (call-p matchers/toHaveAccessibleDescription element))
  ([element expected]
   (call-p matchers/toHaveAccessibleDescription element expected)))

(let [kw (fn [v]
           (if (keyword? v)
             (name v)
             v))]
  (defn attribute=
    "Does this element have the specified attribute set, and does the
  attribute value match the given
  one? See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tohaveattribute)
  for details."
    ([element attr]
     (call-p matchers/toHaveAttribute element (kw attr)))
    ([element attr value]
     (call-p matchers/toHaveAttribute element (kw attr) value))))

(defn class=
  "Does this element have the specified classes set?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tohaveattribute)
  for details."
  ([element]
   (call-p matchers/toHaveClass element))
  ([element classes & [options]]
   (call-p matchers/toHaveClass element (if (string? classes) classes (to-array classes)) #js {"exact" (:exact? options)})))

(defn focused?
  "Does this element have the
  focus? See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tohavefocus)
  for details."
  [element]
  (call-p matchers/toHaveFocus element))

(defn form-values=
  "Does this field or fieldset contain form controls for each given name
  and with the expected
  values? See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tohaveformvalues)
  for details."
  [element expected-values]
  (call-p matchers/toHaveFormValues element (clj->js expected-values)))

(defn- camelize
  ;; NOTE: copied from reacl-c.impl.react0 - but better than nothing.
  "Camelcases a hyphenated string, for example:
  > (camelize \"background-color\")
  < \"backgroundColor\""
  [s]
  (str/replace s #"-(.)" (fn [[_ c]] (str/upper-case c))))

(defn- map-keys [f m]
  (-> (reduce-kv (fn [r k v]
                   (assoc! r (f k) v))
                 (transient {})
                 m)
      (persistent!)))

(defn style=
  "Does this element have the specified styles as inline styles or via
  CSS? See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tohavestyle)
  for details."
  [element style]
  ;; Note: style can be a map, but most probably the property mapping is not identical to React's (and reacl-c's) way. No easy way to change that :-/
  (call-p matchers/toHaveStyle element (if (map? style)
                                         (clj->js (map-keys camelize style))
                                         style)))

(defn text-content=
  "Does this element has the specified text content?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tohavetextcontent)
  for details."
  ([element text]
   (text-content= element text nil))
  ([element text options]
   (call-p matchers/toHaveTextContent element text (if (and (map? options) (contains? options :normalize-whitespace?))
                                                  #js {"normalizeWhitespace" (:normalize-whitespace? options)}
                                                  #js {}))))

(defn value=
  "Does this form element have the specified
  value? See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tohavevalue)
  for details."
  [element value]
  (call-p matchers/toHaveValue element value))

(defn display-value=
  "Does this form element have the specified value displayed to the
  user?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tohavedisplayvalue)
  for details."
  [element value]
  (call-p matchers/toHaveDisplayValue element value))

(defn checked?
  "Is this form element checked?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tobechecked)
  for details."
  [element]
  (call-p matchers/toBeChecked element))

(defn partially-checked?
  "Is this form element partially checked?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tobepartiallychecked)
  for details."
  [element]
  (call-p matchers/toBePartiallyChecked element))

(defn error-message=
  "Does this element have the specified aria error message?
  See [here](https://github.com/testing-library/jest-dom/tree/v5.16.4#tohaveerrormessage)
  for details."
  [element text]
  (call-p matchers/toHaveErrorMessage element text))

